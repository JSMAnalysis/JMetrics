#!/usr/bin/env python3

'''
    Python script to visualize dependencies data from a csv file.
    The plot displays relationship between afferent coupling and efferent coupling.
'''

from dependency import *
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
import sys
import mplcursors



def setupPositionStructure():
    ''' Creates an (average time complexity) O(1) granules access structure.
    Dict with tuple (ca, ce) as key; granule index as value) '''
    global positions
    positions = dict()
    for k in range(len(granules)):
        ca = granules[k].getCaArity()
        ce = granules[k].getCeArity()
        tuple = (datax[k], datay[k])
        if positions.get(tuple) == None:
            positions[tuple] = [k]
        else:
            positions.get(tuple).append(k)



def coupling_2D_axis():
    global granules, dependencies, labels
    granules, dependencies = setupDependenciesData(sys.argv[1])
    labels = [g.name for g in granules]

    global datax, datay
    datax = [g.getCaArity() for g in granules]
    datay = [g.getCeArity() for g in granules]
    setupPositionStructure()

    maxCaArity = max(g.getCaArity() for g in granules)
    maxCeArity = max(g.getCeArity() for g in granules)

    fig, axes = plt.subplots(figsize=(6, 6))
    plt.xlim(-1, maxCaArity + 1)
    axes.yaxis.set_major_locator(MaxNLocator(integer=True))
    plt.ylim(-1, maxCeArity + 1)

    plt.xlabel("Afferent Coupling (Ca)")
    plt.ylabel("Efferent Coupling (Ce)")
    plt.title("Relationship between Afferent Coupling and Efferent Coupling", y=1)

    granules = plt.scatter(datax, datay)
    mplcursors.cursor(granules, hover=True, highlight=True).connect("add", setPointHover)

    plt.show()



def setPointHover(sel):
    index = int(sel.target.index)
    ca, ce = datax[index], datay[index]
    loc = "[Ca = " + str(ca) + "; Ce = " + str(ce) + "]"
    div = ce / (ca + ce) if (ca != 0 or ce != 0) else 0.0
    instability = "[I = Ce / (Ca + Ce) = " + str(round(div, 2)) + "]\n"
    pointsOnSamePos = positions[(datax[index], datay[index])]
    granulesDisplay = [labels[i] for i in pointsOnSamePos]
    sel.annotation.set_text(loc + " - " + instability + '\n'.join(granulesDisplay))



if __name__ == "__main__":
    coupling_2D_axis()
