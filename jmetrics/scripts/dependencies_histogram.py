#!/usr/bin/env python3

'''
    Python script to visualize dependencies data from a csv file.
'''
USAGE = "python dependencies_histogram.py <csv_file> <ca|ce>"

from retrieve_csv_data import retrieve_data
import numpy as np
import matplotlib.pyplot as plt
import sys



def setupData():
    csv = retrieve_data(sys.argv[1], ("GranuleSrc", "GranuleDst", "Type", "Arity"))
    dependencies = []
    for i in range(len(csv)):
        dependencies.append(Dependency(csv[i][0], csv[i][1], csv[i][2], csv[i][3]))
    granulesSrc = set([d.src for d in dependencies])
    granulesDst = set([d.dst for d in dependencies])
    granuleNames = granulesSrc.union(granulesDst)
    granules = []
    for g in granuleNames:
        afferentDependencies = list(filter(lambda d: d.src == g, dependencies))
        efferentDependencies = list(filter(lambda d: d.dst == g, dependencies))
        granules.append(Granule(g, afferentDependencies, efferentDependencies))
    return granules, dependencies



def dependencies_histogram(analysis):
    granules, dependencies = setupData()
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





class Granule:
    def __init__(self, name, afferentDependencies, efferentDependencies):
        self.name = name
        self.afferentDependencies = afferentDependencies
        self.efferentDependencies = efferentDependencies
    def getCaArity(self):
        return sum(d.arity for d in self.afferentDependencies)
    def getCeArity(self):
        return sum(d.arity for d in self.efferentDependencies)
    def getCaBarInfo(self):
        return Granule.getBarInfo(self.afferentDependencies)
    def getCeBarInfo(self):
        return Granule.getBarInfo(self.efferentDependencies)
    def getBarInfo(dependencies):
        return [
            len([d for d in dependencies if d.type == "Inheritance"]),
            len([d for d in dependencies if d.type == "Aggregation"]),
            len([d for d in dependencies if d.type == "UseLink"])
        ]





class Dependency:
    '''Reprensents a dependency between 2 granules.'''
    def __init__(self, src, dst, type, arity):
        self.src = src
        self.dst = dst
        self.type = type
        self.arity = arity
    DependencyType = {
        "Inheritance":  "red",
        "Aggregation":  "blue",
        "UseLink":      "green"
    }



CA_ANALYSIS = "ca"
CE_ANALYSIS = "ce"
if __name__ == "__main__":
    if len(sys.argv) != 3 or (sys.argv[2] != CA_ANALYSIS and sys.argv[2] != CE_ANALYSIS):
        print("Bad CLI args.")
        print("Usage: " + USAGE)
        exit(1)
    dependencies_histogram(sys.argv[2])
