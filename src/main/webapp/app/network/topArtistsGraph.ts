import * as d3 from 'd3';

interface Node extends d3.SimulationNodeDatum {
  id: number;
  name: string;
  type: string;
  genre: string;
  img: string;
  rank: number;
  x?: number;
  y?: number;
  card?: d3.Selection<HTMLDivElement, unknown, null, undefined>;
  songsInLibrary: number;
}

interface Link extends d3.SimulationLinkDatum<Node> {
  source: number | string;
  target: number | string;
  distance: number;
}

interface Artist {
  distance: number;
  name: string;
  genres: string[];
  imageUrl: string;
  id: string;
  songsInLibrary: number;
}

function getElements(artists: Artist[], userImg: string, connections: { [key: string]: string[] }): { nodes: Node[]; links: Link[] } {
  let nextId = 1;
  const nodes: Node[] = [{ id: 0, name: 'User', type: 'user', genre: '', img: userImg, rank: 0, songsInLibrary: 0 }];
  const idMap: { [key: string]: number } = { User: 0 };
  const links: Link[] = [];

  artists.forEach(artist => {
    const nodeId = nextId++;
    nodes.push({
      id: nodeId,
      name: artist.name,
      type: 'artist',
      genre: artist.genres[0] || 'unknown',
      img: artist.imageUrl,
      rank: artist.distance,
      songsInLibrary: artist.songsInLibrary || 0,
    });
    idMap[artist.id] = nodeId; // Mapping external artist ID to internal node ID
  });

  artists.forEach(artist => {
    // Adding links from user to each artist
    links.push({
      source: 0,
      target: idMap[artist.id],
      distance: artist.distance,
    });

    // Adding artist-to-artist connections
    if (connections[artist.id]) {
      connections[artist.id].forEach(connectedArtistId => {
        if (idMap[connectedArtistId]) {
          links.push({
            source: idMap[artist.id],
            target: idMap[connectedArtistId],
            distance: 600, // Arbitrary distance or derived value
          });
        }
      });
    }
  });

  return { nodes, links };
}

function renderGraph(graphContainer: any, width: number, height: number, nodes: Node[], links: Link[]): void {
  const svg = d3
    .select(graphContainer)
    .append('svg')
    .attr('width', width)
    .attr('height', height)
    .style('animation', 'subtle-zoom 5s infinite alternate ease-in-out');

  const scaleDistance = (d: any) => {
    const scale = width / 1000; // Calculate the scaling factor based on current width
    return d.distance * scale; // Scale the distance based on the current container width
  };

  const defs = svg.append('defs');
  nodes.forEach((node, index) => {
    const sanitizedId = encodeURIComponent(node.id).replace(/[!'()*]/g, '');
    const imageSize = node.type === 'user' ? width / 17.82 : width / 22.275;

    defs
      .append('pattern')
      .attr('id', `img-${node.id}`)
      .attr('patternUnits', 'objectBoundingBox')
      .attr('width', 1)
      .attr('height', 1)
      .append('image')
      .attr('xlink:href', node.img)
      .attr('width', imageSize)
      .attr('height', imageSize)
      .attr('preserveAspectRatio', 'xMidYMid slice');
  });

  nodes.forEach((node, index) => {
    if (node.type === 'artist') {
      const sanitizedId = encodeURIComponent(node.id).replace(/[!'()*]/g, '');
      const nodeCard = d3
        .select(graphContainer)
        .append('div')
        .attr('class', 'node-card')
        .attr('id', `node-card-${sanitizedId}`)
        .html(
          `
          <center><img src="${node.img}" alt="Artist Image" style="width: 80px; height: 80px; border-radius: 0.375rem; box-shadow: 0 0 1px white;"></center>
          <div><center><strong>${node.name}</strong></center></div>
          <div><center><strong>${node.genre}</strong></center></div>
          <br>
          <div style="font-size: 25px; text-align: center;"><strong>${node.songsInLibrary}</strong></div>
          <div><center>Songs in Library</center></div>
        `
        )
        .style('width', `${width / 5}px`)
        .style('color', 'white')
        .style('position', 'absolute')
        .style('visibility', 'hidden')
        .style('background-color', '#383838')
        .style('padding', '10px')
        .style('border-radius', '8px')
        .style('pointer-events', 'none')
        .style('z-index', '1000');

      node.card = nodeCard;
    }
  });

  const nodeRadius = (d: any) => (d.type === 'user' ? width / 50 : width / 60);

  const simulation = d3
    .forceSimulation(nodes)
    .alpha(0.095) // Start with a higher initial alpha for more initial motion
    .alphaDecay(0.02) // Lower decay rate to prolong the simulation
    .velocityDecay(0.9)
    .force(
      'link',
      d3
        .forceLink(links)
        .id((d: any) => d.id)
        .distance(d => d.distance)
    )
    .force(
      'charge',
      d3.forceManyBody().strength((d: any) => (d.type === 'user' ? -1500 : -50))
    )
    .force('center', d3.forceCenter(width / 2, height / 2))
    .force('collision', d3.forceCollide().radius(nodeRadius))
    .force(
      'link',
      d3
        .forceLink(links)
        .id((d: any) => d.id)
        .distance(scaleDistance)
    )
    .force(
      'fixUserX',
      d3.forceX(width / 2).strength((d: any) => (d.type === 'user' ? 1 : 0))
    )
    .force(
      'fixUserY',
      d3.forceY(height / 2).strength((d: any) => (d.type === 'user' ? 1 : 0))
    );

  const link = svg.selectAll('line').data(links).enter().append('line').style('stroke', 'black').style('stroke-width', 0.2);

  const node = svg
    .selectAll('circle')
    .data(nodes)
    .enter()
    .append('circle')
    .attr('r', d => (d.type === 'user' ? width / 35.64 : width / 44.55))
    .attr('class', d => (d.type === 'user' ? 'user-node' : 'normal-node'))
    .style('fill', d => `url(#img-${encodeURIComponent(d.id).replace(/[!'()*]/g, '')})`)
    .style('stroke', 'black')
    .style('stroke-width', 0.75)
    // When setting the event listeners
    .on('mouseover', (event, d) => {
      const cardSelector = `#node-card-${d.id}`; // d.id is now numeric, which simplifies selectors
      d3.select(cardSelector).style('visibility', 'visible');
    })
    .on('mouseleave', (event, d) => {
      const cardSelector = `#node-card-${d.id}`;
      d3.select(cardSelector).style('visibility', 'hidden');
    });
  simulation.on('tick', () => updateGraph(node, link, width, height));
  // applyVisualOscillationToNodes(svg, nodes);
}

function updateGraph(node: any, link: any, width: number, height: number) {
  node
    .attr('cx', (d: any) => {
      const radius = d.type === 'user' ? 25 : 20;
      return Math.max(radius, Math.min(width - radius, d.x));
    })
    .attr('cy', (d: any) => {
      const radius = d.type === 'user' ? 25 : 20;
      return Math.max(radius, Math.min(height - radius, d.y));
    });

  node.each(function (d: any) {
    if (d.card) {
      const radius = d.type === 'user' ? 25 : 20;
      const cardHeight = 178;
      const offset = width / 89;

      let topPosition;

      if (d.y >= height / 2) {
        topPosition = d.y - 1.75 * radius - cardHeight;
      } else {
        topPosition = d.y + cardHeight / 2;
      }

      d.card.style('left', `${d.x + offset}px`).style('top', `${topPosition}px`);
    }
  });

  link
    .attr('x1', (d: any) => d.source.x)
    .attr('y1', (d: any) => d.source.y)
    .attr('x2', (d: any) => d.target.x)
    .attr('y2', (d: any) => d.target.y);
}

function clearGraph(graphContainer: any): void {
  const svg = d3.select(graphContainer).select('svg');
  svg.remove();

  const nodeCards = d3.select(graphContainer).selectAll('.node-card');
  nodeCards.remove();
}

export { getElements, renderGraph, clearGraph };
