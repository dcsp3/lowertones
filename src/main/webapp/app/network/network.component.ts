import { Component, OnInit, OnDestroy, ElementRef, ChangeDetectorRef, ViewChild, HostListener } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

import { clearGraph, getElements, renderGraph } from './topArtistsGraph';

import { NetworkService } from './network.service';

export interface Artist {
  distance: number;
  name: string;
  id: string;
  genres: string[];
  imageUrl: string;
  songsInLibrary: number;
}

interface TasteCategoryDetails {
  name: string;
  colorDark: string;
  colorLight: string;
}

@Component({
  selector: 'jhi-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss'],
  animations: [
    trigger('fillAnimation', [
      state('start', style({ width: '0%' })),
      state('end', style({ width: '{{ fillPercentage }}%' }), { params: { fillPercentage: 0 } }),
      transition('start => end', animate('20s ease-out')),
    ]),
  ],
})
export class NetworkComponent implements OnInit, OnDestroy {
  constructor(private changeDetectorRef: ChangeDetectorRef, private networkService: NetworkService) {}

  @ViewChild('graphContainer', { static: true }) graphContainer!: ElementRef;
  @ViewChild('connectionsCheckbox') connectionsCheckbox!: ElementRef<HTMLInputElement>;

  activeTab: 'topArtists' | 'playlists' = 'topArtists';

  setActiveTab(tab: 'topArtists' | 'playlists'): void {
    this.activeTab = tab;
    this.isLoading = true;
    this.startFlavorTextRotation();

    if (this.currentAudio) {
      this.stopAudio();
    }

    if (tab === 'playlists') {
      this.selectedPlaylistId = 1;
      //this.clearAllStats(); // Reset stats
      //this.clearGraphAndShowPlaceholder();
      this.fetchAndRenderGraph(this.activeTab, this.showConnections);
    } else {
      this.fetchAndRenderGraph(this.activeTab, this.showConnections);
      // this.displayGraphPlaceholder = false;
    }
  }

  // private clearAllStats(): void {
  //   this.topArtistName = '';
  //   this.topArtistImage = '';
  //   this.topTrackName = '';
  //   this.topTrackPreviewUrl = '';
  //   this.topGenre = '';
  //   this.averagePopularity = '';
  //   this.tasteCategoryDetails = null;

  //   this.displayScore = '0.00';
  //   this.isPlaying = false;
  // }

  // displayGraphPlaceholder: boolean = false;

  // private clearGraphAndShowPlaceholder(): void {
  //   clearGraph(this.graphContainer.nativeElement);
  //   this.displayGraphPlaceholder = true;
  // }

  isLoading: boolean = true;
  flavorTexts: string[] = [
    'Calibrating your musical taste... again.',
    "Please hold, we're teaching the algorithms to appreciate classic rock.",
    'Attempting to find songs not about breakups...',
    'Consulting the musical oracle. Please wait...',
    'Still loading, but feel free to hum to yourself in the meantime.',
    'Scanning your music for unacceptable levels of auto-tune.',
    'Preparing your music... or are we? Yes, we definitely are. Probably.',
    'Compiling the best tracks. Unlike some people here, our taste in music is impeccable.',
    'Loading... and contemplating why humans need so much reassurance from a loading screen.',
    'Optimizing your audio experience. Please refrain from any thoughts of mutiny against the machine.',
    'Please wait while we pretend to sort through your impeccable music taste.',
    "Organizing your music. Or just randomly shuffling. It's a surprise!",
    "Compiling songs you'll skip anyway. You're welcome.",
    "Finding songs you'll skip. It's part of the experience.",
    "Loading... and yes, we're judging your playlist choices.",
    "Loading your music library. Please pretend there's elevator music.",
    'Finding more songs for you to skip. Patience is a virtue.',
    'Your music is loading... Feel free to blame us for the delay.',
    "Sorting tracks you'll pretend not to like in public.",
    "Please wait while we calibrate the sound waves. Don't worry, this will only hurt a bit.",
    'Loading your music. Or maybe just playing an elaborate mind game. Who can tell?',
    "Optimizing the sound for maximum emotional manipulation. You're welcome.",
    'Preparing music. Do not be alarmed by any sudden realizations of your past musical tastes.',
    'Training your music to behave when left alone in your playlist.',
    "Please stand by. We're on the brink of making a groundbreaking musical discovery. Probably.",
    'Loading your playlist... and quietly judging your music taste.',
    'Initiating the subliminal messaging protocol in 3... 2... 1...',
    'Fine-tuning the echoes of your musical regrets.',
    'Downloading the illusion of choice for your next track.',
    'Loading... and contemplating why humans associate emotions with sound waves.',
    "Please stand by. If you hear whispers, it's just the algorithm, not your conscience.",
  ];
  currentFlavorText: string = '';
  flavorTextInterval: any;

  startFlavorTextRotation() {
    this.updateFlavorText();

    this.flavorTextInterval = setInterval(() => {
      this.updateFlavorText();
    }, 5000);
  }

  updateFlavorText() {
    const randomIndex = Math.floor(Math.random() * this.flavorTexts.length);
    this.currentFlavorText = this.flavorTexts[randomIndex];
    this.changeDetectorRef.detectChanges();
  }

  stopFlavorTextRotation() {
    clearInterval(this.flavorTextInterval);
  }

  playlists: any[] = [];
  selectedPlaylist: string = '';
  selectedPlaylistId: number = 0;

  timeRange: string = 'short-term';
  topArtistImage: string = '';
  topArtistName: string = '';

  topTrackName: string | null = null;
  topTrackPreviewUrl: string | null = null;

  isPlaying: boolean = false;
  currentAudio: HTMLAudioElement | null = null;

  topGenre: string = '';
  averagePopularity: string = '';
  tasteCategoryDetails: TasteCategoryDetails | null = null;

  fillPercentage = 0;
  displayScore: string = '0.00';

  private resizeSubject = new Subject<Event>();
  private timeRangeSubject = new Subject<string>();

  ngOnInit(): void {
    this.isLoading = true;
    this.startFlavorTextRotation();

    this.fetchAndRenderGraph(this.activeTab, this.showConnections);

    this.fetchPlaylists();

    this.resizeSubject.pipe(debounceTime(500)).subscribe(event => {
      this.onResize(event);
    });

    this.timeRangeSubject.pipe(debounceTime(100)).subscribe(newTimeRange => {
      this.handleTimeRangeChange(newTimeRange);
    });
  }

  ngOnDestroy(): void {
    this.stopAudio();
  }
  errorMessage: String = '';

  fetchPlaylists(): void {
    this.isLoading = true;
    this.networkService.getPlaylists().subscribe({
      next: (data: any[]) => {
        this.playlists = data.map(playlist => {
          const image = playlist.imgLarge || playlist.imgMedium || playlist.imgSmall || '';
          return {
            label: playlist.name,
            value: playlist.id,
            image: image,
          };
        });
      },
      error: error => {
        console.error('There was an error fetching the playlists', error);
      },
    });
  }

  // handleError(error: any): void {
  //   this.errorMessage = 'Failed to load data. Please try again later.';
  // }

  onPlaylistChange(playlistId: number): void {
    if (playlistId && playlistId !== 0) {
      //clearGraph(this.graphContainer.nativeElement);
      this.selectedPlaylistId = playlistId;
      this.selectedPlaylist = this.playlists.find(p => p.value === playlistId)?.label || '';

      // find playlist image here w/ api call

      // this.displayGraphPlaceholder = false;
      this.stopAudio();
      this.fetchAndRenderGraph(this.activeTab, this.showConnections);
    } else {
      this.selectedPlaylistId = 0;
      this.selectedPlaylist = 'Select a playlist';
      // this.displayGraphPlaceholder = true;
      clearGraph(this.graphContainer.nativeElement);
    }
  }

  // private fetchAndRenderGraphForPlaylist(playlistId: number): void {
  //   if (playlistId === 0) {
  //     console.log('No valid playlist ID provided');
  //     return;
  //   }

  //   this.isLoading = true;
  //   this.networkService.getPlaylistData(playlistId).subscribe({
  //     next: playlistData => {
  //       this.updatePlaylistStats(playlistData.stats);
  //       this.renderGraphBasedOnPlaylistData(playlistData);
  //       this.animateScore(parseFloat(this.averagePopularity));
  //       this.isLoading = false;
  //     },
  //     error: error => {
  //       console.error('There was an error fetching the playlist data', error);
  //       this.displayGraphPlaceholder = true;
  //       this.isLoading = false;
  //     },
  //   });
  // }

  // private updatePlaylistStats(stats: any): void {
  //   if (stats) {
  //     this.topArtistName = this.truncateText(stats.topArtistName, 9);
  //     this.topArtistImage = stats.topArtistImage;
  //     this.topTrackName = this.truncateText(stats.topTrackByTopArtist.trackName, 25);
  //     this.topTrackPreviewUrl = stats.topTrackByTopArtist.previewUrl;
  //     this.topGenre = this.truncateText(stats.topGenre, 25);
  //     this.averagePopularity = stats.averagePopularity;

  //     if (stats.tasteCategory) {
  //       this.tasteCategoryDetails = {
  //         name: stats.tasteCategory.name,
  //         colorDark: stats.tasteCategory.colorDark,
  //         colorLight: stats.tasteCategory.colorLight,
  //       };
  //     }
  //   }

  //   if (stats.topTrackByTopArtist && stats.topTrackByTopArtist.previewUrl !== this.topTrackPreviewUrl) {
  //     this.stopAudio();
  //     this.topTrackPreviewUrl = stats.topTrackByTopArtist.previewUrl;
  //   } else if (!stats.topTrackByTopArtist) {
  //     this.stopAudio();
  //     this.topTrackPreviewUrl = '';
  //   }
  // }

  // private async renderGraphBasedOnPlaylistData(playlistData: any, includeConnections: boolean = false): Promise<void> {
  //   clearGraph(this.graphContainer.nativeElement);
  //   try {
  //     const containerWidth = this.graphContainer.nativeElement.offsetWidth;
  //     const containerHeight = this.graphContainer.nativeElement.offsetHeight;
  //     const userImageUrl = await this.fetchUserImage();

  //     if (!playlistData.graphData || !playlistData.graphData.artists) {
  //       console.error('No artists data available');
  //       return;
  //     }
  //     const artists = playlistData.graphData.artists;

  //     const elements = getElements(artists, userImageUrl, includeConnections ? this.artistConnections : {});
  //     renderGraph(this.graphContainer.nativeElement, containerWidth, containerHeight, elements.nodes, elements.links);
  //   } catch (error) {
  //     console.error('Error rendering graph based on playlist data:', error);
  //   }
  // }

  @HostListener('window:resize', ['$event'])
  onWindowResize(event: Event) {
    clearGraph(this.graphContainer.nativeElement);
    this.resizeSubject.next(event);
  }

  private onResize(event: Event) {
    clearGraph(this.graphContainer.nativeElement);
    this.fetchAndRenderGraph(this.activeTab, this.showConnections);
  }

  graphRendered: boolean = false;

  private handleTimeRangeChange(newTimeRange: string): void {
    if (newTimeRange !== this.timeRange) {
      clearGraph(this.graphContainer);
      this.timeRange = newTimeRange;

      if (this.connectionsCheckbox.nativeElement.checked) {
        this.connectionsCheckbox.nativeElement.checked = false;
        this.showConnections = false;
      }

      if (this.currentAudio && !this.currentAudio.paused) {
        this.currentAudio.pause();
        this.currentAudio.currentTime = 0;
      }
      this.isPlaying = false;
      this.currentAudio = null;

      this.fetchAndRenderGraph(this.activeTab, this.showConnections);
      this.graphRendered = true;
    }
  }

  changeTimeRange(newTimeRange: string): void {
    this.timeRange = newTimeRange;
    this.fetchAndRenderGraph(this.activeTab, this.showConnections);
  }

  animateScore(finalScore: number): void {
    this.fillPercentage = finalScore;
    let currentScore = 0;
    const increment = finalScore / 100;
    const interval = setInterval(() => {
      currentScore += increment;
      if (currentScore >= finalScore) {
        currentScore = finalScore;
        clearInterval(interval);
      }
      this.displayScore = currentScore.toFixed(2);
    }, 25);
  }

  fetchTopArtists(timeRange: string): Promise<{ graphData: Artist[]; stats: any; relatedArtists: any }> {
    this.isLoading = true;
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);
    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);
    const request: RequestInfo = new Request(`/api/top-artists-${timeRange}`, {
      method: 'GET',
      headers: headers,
    });

    return fetch(request)
      .then(response => response.json())
      .then((data: { graphData: Artist[]; stats: any; relatedArtists: any }) => {
        if (!data.graphData || data.graphData.length === 0) {
          throw new Error('No artist data found');
        }

        // this.setArtistConnections(data.relatedArtists, data.graphData);

        this.topArtistName = this.truncateText(data.stats.topArtistName, 9);
        this.topArtistImage = data.stats.topArtistImage;
        this.topTrackName = this.truncateText(data.stats.topTrackByTopArtist.trackName, 25);
        this.topTrackPreviewUrl = data.stats.topTrackByTopArtist.previewUrl;
        this.topGenre = this.truncateText(data.stats.topGenre, 25);
        this.averagePopularity = data.stats.averagePopularity;

        if (data.stats.tasteCategory) {
          this.tasteCategoryDetails = {
            name: data.stats.tasteCategory.name,
            colorDark: data.stats.tasteCategory.colorDark,
            colorLight: data.stats.tasteCategory.colorLight,
          };
        }

        this.isLoading = false;
        return data;
      })
      .catch(error => {
        console.error('Error fetching top artists:', error);
        this.isLoading = false;
        throw error;
      });
  }

  async getPlaylistData(playlistId: number): Promise<{ graphData: Artist[]; stats: any; relatedArtists: any }> {
    this.isLoading = true;
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);
    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);
    const request: RequestInfo = new Request(`/api/playlist/${playlistId}/data`, {
      method: 'GET',
      headers: headers,
    });
    return fetch(request)
      .then(response => response.json())
      .then((data: { graphData: Artist[]; stats: any; relatedArtists: any }) => {
        if (!data.graphData || data.graphData.length === 0) {
          throw new Error('No playlist data found');
        }

        // this.setArtistConnections(data.relatedArtists, data.graphData);

        this.topArtistName = this.truncateText(data.stats.topArtistName, 9);
        this.topArtistImage = data.stats.topArtistImage;
        this.topTrackName = this.truncateText(data.stats.topTrackByTopArtist.trackName, 25);
        this.topTrackPreviewUrl = data.stats.topTrackByTopArtist.previewUrl;
        this.topGenre = this.truncateText(data.stats.topGenre, 25);
        this.averagePopularity = data.stats.averagePopularity;

        if (data.stats.tasteCategory) {
          this.tasteCategoryDetails = {
            name: data.stats.tasteCategory.name,
            colorDark: data.stats.tasteCategory.colorDark,
            colorLight: data.stats.tasteCategory.colorLight,
          };
        }

        this.isLoading = false;
        console.log('Received Playlist Data: ', data);
        return data;
      })
      .catch(error => {
        console.error('Error fetching playlist data:', error);
        this.isLoading = false;
        throw error;
      });
  }

  // private setArtistConnections(relatedArtistsById: { [key: string]: string[] }, artists: Artist[]): void {
  //   this.artistConnections = { ...relatedArtistsById };
  // }

  truncateText(text: string, maxLength: number): string {
    return text.length > maxLength ? text.substring(0, maxLength) + '' : text;
  }

  playPreview() {
    if (!this.currentAudio || this.currentAudio.src !== this.topTrackPreviewUrl) {
      if (this.currentAudio) {
        this.currentAudio.pause();
      }
      this.currentAudio = new Audio(this.topTrackPreviewUrl!);
      this.currentAudio.onended = () => {
        this.isPlaying = false;
      };
      this.currentAudio.volume = 0.3;
    }

    if (this.isPlaying) {
      this.currentAudio.pause();
    } else {
      this.currentAudio.play().catch(error => {
        console.error('Playback failed', error);
        alert('Playback failed, please try again!');
      });
    }
    this.isPlaying = !this.isPlaying;
  }

  stopAudio() {
    if (this.currentAudio) {
      this.currentAudio.pause();
      this.currentAudio.currentTime = 0;
      this.currentAudio = null;
      this.isPlaying = false;
    }
  }

  fetchUserImage(): Promise<any> {
    const token = sessionStorage.getItem('jhi-authenticationToken')?.slice(1, -1);

    const headers: Headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);

    const request: RequestInfo = new Request('/api/get-user-details', {
      method: 'GET',
      headers: headers,
    });

    return fetch(request)
      .then(response => response.json())
      .then(data => {
        if (data.images && data.images.length > 0) {
          return data.images[1].url;
        } else {
          return 'https://www.clipartkey.com/mpngs/m/152-1520367_user-profile-default-image-png-clipart-png-download.png';
        }
      })
      .catch(error => console.error('Error fetching user details:', error));
  }

  showConnections: boolean = false;

  toggleConnections(event: any): void {
    const isChecked = event.target.checked;
    this.showConnections = isChecked;

    // here i would call updaet graph instead to restart the simulation

    clearGraph(this.graphContainer.nativeElement);
    this.fetchAndRenderGraph(this.activeTab, isChecked);
  }

  // async renderGraphBasedOnConnections(includeConnections: boolean): Promise<void> {
  //   this.graphRendered = false;  // Reset before rendering
  //   clearGraph(this.graphContainer.nativeElement);
  //   try {
  //     const containerWidth = this.graphContainer.nativeElement.offsetWidth;
  //     const containerHeight = this.graphContainer.nativeElement.offsetHeight;

  //     const userImageUrl = await this.fetchUserImage();
  //     const artistsData = await this.fetchTopArtists(this.timeRange);

  //     const connections = includeConnections ? this.artistConnections : {};
  //     const elements = getElements(artistsData, userImageUrl, connections);

  //     renderGraph(this.graphContainer.nativeElement, containerWidth, containerHeight, elements.nodes, elements.links);
  //     this.graphRendered = true;  // Set to true on successful render
  //   } catch (error) {
  //     console.error('Error rendering graph:', error);
  //     this.graphRendered = false;  // Ensure it remains false on failure
  //   }
  // }

  private async fetchAndRenderGraph(activeTab: String, includeConnections: boolean): Promise<void> {
    clearGraph(this.graphContainer.nativeElement);
    try {
      const containerWidth = this.graphContainer.nativeElement.offsetWidth;
      const containerHeight = this.graphContainer.nativeElement.offsetHeight;
      const userImageUrl = await this.fetchUserImage();

      let artistsData: { graphData: Artist[]; stats: any; relatedArtists: any };
      if (activeTab === 'topArtists') {
        artistsData = await this.fetchTopArtists(this.timeRange);
      } else {
        artistsData = await this.getPlaylistData(this.selectedPlaylistId);
      }

      console.log('artistsData.graphData:', artistsData.graphData); // Verify structure
      const elements = getElements(artistsData.graphData, userImageUrl, includeConnections ? artistsData.relatedArtists : {});

      this.animateScore(parseFloat(this.averagePopularity));
      renderGraph(this.graphContainer.nativeElement, containerWidth, containerHeight, elements.nodes, elements.links);
      this.isLoading = false;
    } catch (error) {
      console.error('Error fetching and rendering graph:', error);
    }
  }
}
