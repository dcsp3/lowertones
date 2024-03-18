import * as d3 from 'd3';

interface Node extends d3.SimulationNodeDatum {
  id: string;
  type: string;
  img: string;
  rank: number;
  x?: number;
  y?: number;
}

interface Link extends d3.SimulationLinkDatum<Node> {
  source: string;
  target: string;
  distance: number;
}

function getElements(termArray: [number, string, Array<string>, string, string][], userImg: string): { nodes: Node[]; links: Link[] } {
  const nodes: Node[] = [
    { id: 'user', type: 'user', img: userImg, rank: 0 }, // Assuming the user has rank 0 initially
  ];

  for (const [rank, artist, genres, id, img] of termArray) {
    nodes.push({ id: artist, type: 'artist', img, rank });
  }

  const links: Link[] = [];

  for (const node of nodes) {
    if (node.type === 'user') continue; // Skip the user node
    links.push({ source: 'user', target: node.id, distance: node.rank });
  }

  return { nodes, links };
}

// On hover functions
// Get these working

/*
function handleMouseOver(event: any, d: Node) {
  const currentNode = d3.select(event.currentTarget);
  currentNode.attr('r', (d.type === 'user' ? 28 : 23));
  d3.select(`#label-${encodeURIComponent(d.id).replace(/[!'()*]/g, '')})`).style('display', 'block');
}

function handleMouseOut(event: any, d: Node) {
  const currentNode = d3.select(event.currentTarget);
  currentNode.attr('r', (d.type === 'user' ? 25 : 20));
  d3.select(`#label-${encodeURIComponent(d.id).replace(/[!'()*]/g, '')})`).style('display', 'none');
}
*/

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

  function drag(simulation: d3.Simulation<Node, undefined>) {
    function dragstarted(event: d3.D3DragEvent<SVGCircleElement, any, any>) {
      if (!event.active) simulation.alphaTarget(0.3).restart();
      event.subject.fx = event.subject.x;
      event.subject.fy = event.subject.y;
    }

    function dragged(event: d3.D3DragEvent<SVGCircleElement, any, any>) {
      event.subject.fx = event.x;
      event.subject.fy = event.y;
    }

    function dragended(event: d3.D3DragEvent<SVGCircleElement, any, any>) {
      if (!event.active) simulation.alphaTarget(0);
      event.subject.fx = null;
      event.subject.fy = null;
    }

    return d3.drag<SVGCircleElement, any>().on('start', dragstarted).on('drag', dragged).on('end', dragended);
  }

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
    .call(drag(simulation));
  //.on('mouseenter', handleMouseOver)
  //.on('mouseleave', handleMouseOut);

  const label = svg
    .selectAll('text')
    .data(nodes)
    .enter()
    .append('text')
    .text(d => (d.id !== 'user' ? d.id : ''))
    .attr('text-anchor', 'middle')
    .attr('alignment-baseline', 'middle')
    .style('font-size', '12px')
    .style('fill', d => 'white')
    .style('display', 'none')
    .attr('id', d => `label-${encodeURIComponent(d.id).replace(/[!'()*]/g, '')})`);

  simulation.on('tick', () => updateGraph(node, link, label, width, height));
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
}

export { getElements, renderGraph, clearGraph };
