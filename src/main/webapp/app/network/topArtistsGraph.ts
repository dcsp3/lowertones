import * as d3 from 'd3';

interface Node {
  id: string;
  type: string;
  rank: number;
  x?: number;
  y?: number;
}

interface Link {
  source: string;
  target: string;
  distance: number;
}

function getElements(termArray: [number, string][]): { nodes: Node[]; links: Link[] } {
  const nodes: Node[] = [
    { id: 'user', type: 'user', rank: 0 }, // Assuming the user has rank 0 initially
  ];

  for (const [rank, artist] of termArray) {
    nodes.push({ id: artist, type: 'artist', rank });
  }

  const links: Link[] = [];

  for (const node of nodes) {
    if (node.type === 'user') continue; // Skip the user node
    links.push({ source: 'user', target: node.id, distance: node.rank });
  }

  return { nodes, links };
}

function renderGraph(graphContainer: any, width: number, height: number, nodes: Node[], links: Link[]): void {
  const svg = d3.select(graphContainer).append('svg').attr('width', width).attr('height', height);

  const simulation = d3
    .forceSimulation(nodes)
    .alpha(0.5)
    .alphaDecay(0.1)
    .velocityDecay(0.9)
    .force(
      'charge',
      d3.forceManyBody().strength((d: any) => (d.type === 'user' ? -1500 : -100))
    )
    .force('center', d3.forceCenter(width / 2, height / 2))
    .force(
      'collision',
      d3.forceCollide().radius((d: any) => (d.type === 'user' ? 0 : 50))
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
    .style('stroke-dasharray', '5,5');

  const node = svg
    .selectAll('circle')
    .data(nodes)
    .enter()
    .append('circle')
    .attr('r', d => (d.type === 'user' ? 25 : 20))
    .attr('class', d => (d.type === 'user' ? 'user-node' : 'normal-node'))
    .style('fill', d => (d.type === 'user' ? 'red' : 'whitesmoke'));

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
    .style('display', 'block')
    .attr('id', d => `label-${d.id}`);

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

/*
function handleMouseOver(d) {
  const currentNode = d3.select(this).datum();

  d3.select(this).attr('r', (d) => (d.type === 'user' ? 28 : 23));

  label.filter((nodeData) => nodeData.id === currentNode.id).style('display', 'block');
}

function handleMouseOut() {
  const currentNode = d3.select(this).datum();

  d3.select(this).attr('r', (d) => (currentNode.type === 'user' ? 25 : 20));
}

// Add event listeners to the buttons
const shortTermButton = document.getElementById('shortTerm');
const mediumTermButton = document.getElementById('mediumTerm');
const longTermButton = document.getElementById('longTerm');

shortTermButton.addEventListener('click', function () {
  const elements = getElements(short_term);

  svg.selectAll('line').remove();
  svg.selectAll('circle').remove();
  svg.selectAll('text').remove();

  renderGraph(elements.nodes, elements.links);
});

mediumTermButton.addEventListener('click', function () {
  const elements = getElements(medium_term);

  svg.selectAll('line').remove();
  svg.selectAll('circle').remove();
  svg.selectAll('text').remove();

  renderGraph(elements.nodes, elements.links);
});

longTermButton.addEventListener('click', function () {
  const elements = getElements(long_term);

  svg.selectAll('line').remove();
  svg.selectAll('circle').remove();
  svg.selectAll('text').remove();

  renderGraph(elements.nodes, elements.links);
});
*/
