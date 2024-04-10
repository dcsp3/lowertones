import * as d3 from 'd3';

interface Node extends d3.SimulationNodeDatum {
  id: number;
  name: string; // Added field for the artist's name
  type: string;
  genre: string;
  img: string;
  rank: number;
  x?: number;
  y?: number;
}

interface Link extends d3.SimulationLinkDatum<Node> {
  source: number | string; // Support number for numeric IDs, string for 'user'
  target: number | string;
  distance: number;
}

interface Artist {
  distance: number; // Assuming this maps to 'rank' in Node
  name: string;
  genres: string[];
  imageUrl: string;
  id: string;
}

function getElements(artists: Artist[], userImg: string): { nodes: Node[]; links: Link[] } {
  let nextId = 1; // Start ID counter for artists
  const nodes: Node[] = [{ id: 0, name: 'User', type: 'user', genre: '', img: userImg, rank: 0 }];

  for (const artist of artists) {
    const genre = artist.genres[0];
    nodes.push({
      id: nextId++,
      name: artist.name,
      type: 'artist',
      genre: genre,
      img: artist.imageUrl,
      rank: artist.distance,
    });
  }

  const links: Link[] = artists.map((artist, index) => ({
    source: 0, // User node ID
    target: index + 1,
    distance: artist.distance,
  }));

  return { nodes, links };
}

function applyVisualOscillationToNodes(svg: any, nodes: any, frequency = 7000) {
  const amplitude = 0.25; // Maximum distance from the original position

  d3.timer(elapsed => {
    svg
      .selectAll('circle')
      .attr('cx', (d: any) => d.x + Math.sin((elapsed / frequency) * d.id) * amplitude)
      .attr('cy', (d: any) => d.y + Math.cos((elapsed / frequency) * d.id) * amplitude);
  });
}

function renderGraph(graphContainer: any, width: number, height: number, nodes: Node[], links: Link[]): void {
  const svg = d3.select(graphContainer).append('svg').attr('width', width).attr('height', height);

  // Defining patterns for each node based on imageUrl
  const defs = svg.append('defs');
  nodes.forEach((node, index) => {
    const sanitizedId = encodeURIComponent(node.id).replace(/[!'()*]/g, '');
    const imageSize = node.type === 'user' ? 50 : 40; // Larger image size for 'user'

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
      // Sanitize the ID for consistent usage
      const sanitizedId = encodeURIComponent(node.id).replace(/[!'()*]/g, '');
      const nodeCard = d3
        .select('body')
        .append('div')
        .attr('class', 'node-card')
        // Use the sanitized ID here
        .attr('id', `node-card-${sanitizedId}`)
        .html(
          `
          <center><img src="${node.img}" alt="Artist Image" style="width: 80px; height: 80px; border-radius: 0.375rem; box-shadow: 0 0 1px white;"></center>
          <div><center><strong>${node.name}</strong></center></div>
          <div><center><strong>${node.genre}</strong></center></div>
          <br>
          <div style="font-size: 25px; text-align: center;"><strong>25</strong></div>
          <div><center>Songs in Library</center></div>
        `
        )
        .style('width', '200px')
        .style('color', 'white')
        .style('position', 'absolute')
        .style('visibility', 'hidden')
        .style('background-color', '#383838')
        .style('backdrop-filter', 'blur(10px)') // Adjusted value
        .style('padding', '10px')
        .style('border-radius', '8px')
        .style('pointer-events', 'none')
        .style('z-index', '1000');
    }
  });

  const simulation = d3
    .forceSimulation(nodes)
    .alpha(0.5)
    .alphaDecay(0.1)
    .velocityDecay(0.9)
    .force(
      'charge',
      d3.forceManyBody().strength((d: any) => (d.type === 'user' ? -1500 : -50))
    )
    .force('center', d3.forceCenter(width / 2, height / 2))
    .force(
      'collision',
      d3.forceCollide().radius((d: any) => (d.type === 'user' ? 0 : 25))
    )
    .force(
      'link',
      d3
        .forceLink(links)
        .id((d: any) => d.id)
        .distance(d => d.distance)
    )
    .force(
      'fixUserX',
      d3.forceX(width / 2).strength((d: any) => (d.type === 'user' ? 1 : 0))
    )
    .force(
      'fixUserY',
      d3.forceY(height / 2).strength((d: any) => (d.type === 'user' ? 1 : 0))
    );

  const link = svg
    .selectAll('line')
    .data(links)
    .enter()
    .append('line')
    .style('stroke', 'white')
    .style('stroke-width', 0.25)
    .style('stroke-dasharray', '2,2');

  const node = svg
    .selectAll('circle')
    .data(nodes)
    .enter()
    .append('circle')
    .attr('r', d => (d.type === 'user' ? 25 : 20))
    .attr('class', d => (d.type === 'user' ? 'user-node' : 'normal-node'))
    .style('fill', d => `url(#img-${encodeURIComponent(d.id).replace(/[!'()*]/g, '')})`)
    .style('stroke', 'black')
    .style('stroke-width', 0.75)
    // When setting the event listeners
    .on('mouseover', (event, d) => {
      const cardSelector = `#node-card-${d.id}`; // d.id is now numeric, which simplifies selectors
      d3.select(cardSelector)
        .style('left', function () {
          return d.x! + 12.5 + 'px';
        })
        .style('top', function () {
          return d.y! - 95 + 'px';
        })
        .style('visibility', 'visible');
    })
    .on('mouseleave', (event, d) => {
      const cardSelector = `#node-card-${d.id}`;
      d3.select(cardSelector).style('visibility', 'hidden');
    });

  const label = svg
    .selectAll('text')
    .data(nodes)
    .enter()
    .append('text')
    .text(d => (d.name !== 'user' ? d.id : ''))
    .attr('text-anchor', 'middle')
    .attr('alignment-baseline', 'middle')
    .style('font-size', '12px')
    .style('fill', d => 'white')
    .style('display', 'none')
    .attr('id', d => `label-${encodeURIComponent(d.id).replace(/[!'()*]/g, '')})`);

  simulation.on('tick', () => updateGraph(node, link, label, width, height));
  // applyVisualOscillationToNodes(svg, nodes);
}

function updateGraph(node: any, link: any, label: any, width: number, height: number) {
  node
    .attr('cx', (d: any) => {
      const radius = d.type === 'user' ? 25 : 20;
      return Math.max(radius, Math.min(width - radius, d.x));
    })
    .attr('cy', (d: any) => {
      const radius = d.type === 'user' ? 25 : 20;
      return Math.max(radius, Math.min(height - radius, d.y));
    });

  link
    .attr('x1', (d: any) => d.source.x)
    .attr('y1', (d: any) => d.source.y)
    .attr('x2', (d: any) => d.target.x)
    .attr('y2', (d: any) => d.target.y);

  label.attr('x', (d: any) => d.x).attr('y', (d: any) => d.y - 30);
}

function clearGraph(graphContainer: any): void {
  // Use D3 to select and remove all child elements of the graph container
  d3.select(graphContainer).selectAll('*').remove();
  d3.selectAll('.node-card').remove(); // Assumes node cards have 'node-card' class
}

export { getElements, renderGraph, clearGraph };
