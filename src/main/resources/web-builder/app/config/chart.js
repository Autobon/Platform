export default function chartConfig(ChartJsProvider) {
    ChartJsProvider.setOptions({
        responsive         : false,
        maintainAspectRatio: false,
        colours            : ['#FBA828', '#a65106', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
    });
}

chartConfig.$inject = ['ChartJsProvider'];
