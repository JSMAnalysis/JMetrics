#!/usr/bin/env python3

'''
    Python script to visualize dependencies data from a csv file.
    The plot is a dependency type histogram of the afferent or efferent coupling.
'''
USAGE = "python dependencies_histogram.py <csv_file> <ca|ce>"
CA_ANALYSIS = "ca"
CE_ANALYSIS = "ce"


from dependency import *
import numpy as np
import matplotlib.pyplot as plt
import sys


def dependencies_histogram(analysis):
    granules, dependencies = setup_dependencies_data(sys.argv[1])
    nbGranules = len(granules)

    if (analysis == CA_ANALYSIS):
        granules.sort(key=lambda g: sum(g.getCaBarInfo()))
        BarInfos = [granules[i].getCaBarInfo() for i in range(nbGranules)]  
        maxArity = max(g.getCaArity() for g in granules)
        xLabel = "Number of afferent dependencies"
        title = "Afferent Coupling per type"
    else:
        granules.sort(key=lambda g: sum(g.getCeBarInfo()))
        BarInfos = [granules[i].getCeBarInfo() for i in range(nbGranules)]  
        maxArity = max(g.getCeArity() for g in granules)
        xLabel = "Number of efferent dependencies"
        title = "Efferent Coupling per type"

    granuleAxis = [i for i in range(nbGranules)]
    granuleNames = [g.name for g in granules]

    fig, axe = plt.subplots(figsize=(10, 0.2 * nbGranules))
    axe.set_position([0.4, 0.1, 0.55, 0.8])
    plt.yticks(granuleAxis, granuleNames)
    plt.xlim(0, maxArity)
    plt.xlabel(xLabel)
    plt.title(title)

    colors = list(Dependency.DependencyType.values())
    left = np.zeros(nbGranules) # Left-hand offsets
    barContainers = []
    for (i, barInfo) in enumerate(np.transpose(BarInfos)):
        barContainers.append(
            axe.barh(granuleAxis, barInfo, color=colors[i % len(colors)], left=left)
        )
        left += barInfo

    axe.legend(barContainers, list(Dependency.DependencyType.keys()), loc=4)
    plt.show()


if __name__ == "__main__":
    if len(sys.argv) != 3 or (sys.argv[2] != CA_ANALYSIS and sys.argv[2] != CE_ANALYSIS):
        print("Bad CLI args (Require dependency csv file).\nUsage: " + USAGE)
        exit(1)
    dependencies_histogram(sys.argv[2])
