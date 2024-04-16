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
}

function getElements(artists: Artist[], userImg: string): { nodes: Node[]; links: Link[] } {
  let nextId = 1;
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
    source: 0,
    target: index + 1,
    distance: artist.distance,
  }));

  return { nodes, links };
}

function renderGraph(graphContainer: any, nodes: Node[], links: Link[]): void {
  const svg = d3
    .select(graphContainer)
    .append('svg')
    .style('width', '46vw')
    .style('height', '73vh')
    .style('animation', 'subtle-zoom 5s infinite alternate ease-in-out');

  const width = svg.node()!.getBoundingClientRect().width;
  const height = svg.node()!.getBoundingClientRect().height;

  const defs = svg.append('defs');
  nodes.forEach(node => {
    const imageSize = node.type === 'user' ? 50 : 40;

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

  const simulation = d3
    .forceSimulation(nodes)
    .alpha(0.095)
    .alphaDecay(0.02)
    .velocityDecay(0.9)
    .force(
      'charge',
      d3.forceManyBody().strength((d: any) => (d.type === 'user' ? -1500 : -50))
    )
    .force('center', d3.forceCenter(width / 2, height / 2))
    .force(
      'collision',
      d3.forceCollide().radius((d: any) => (d.type === 'user' ? 0 : 15))
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

  const link = svg.selectAll('line').data(links).enter().append('line').style('stroke', 'black').style('stroke-width', 0.2);

  const node = svg
    .selectAll('circle')
    .data(nodes)
    .enter()
    .append('circle')
    .attr('r', d => (d.type === 'user' ? 25 : 20))
    .attr('class', d => (d.type === 'user' ? 'user-node' : 'normal-node'))
    .style('fill', d => `url(#img-${encodeURIComponent(d.id).replace(/[!'()*]/g, '')})`)
    .style('stroke', 'black')
    .style('stroke-width', 0.75);

  simulation.on('tick', () => updateGraph(node, link, width, height));
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

  link
    .attr('x1', (d: any) => d.source.x)
    .attr('y1', (d: any) => d.source.y)
    .attr('x2', (d: any) => d.target.x)
    .attr('y2', (d: any) => d.target.y);
}

function clearGraph(graphContainer: any): void {
  d3.select(graphContainer).selectAll('*').remove();
}

export { getElements, renderGraph, clearGraph };
