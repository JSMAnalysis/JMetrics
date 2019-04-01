#!/usr/bin/env python3

'''
    Python script to visualize dependencies data from a csv file.
    The plot displays relationship between afferent coupling and efferent coupling on 2D axis.
'''
USAGE = "python coupling_2D_axis.py <csv_file>"


from dependency import *
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
import sys
import mplcursors


def setup_position_structure():
    ''' Creates an (average time complexity) O(1) granules access structure.
    Dict with tuple (ca, ce) as key; granule index as value) '''
    global positions
    positions = dict()
    for k in range(len(granules)):
        ca = granules[k].getCaArity()
        ce = granules[k].getCeArity()
        t = (datax[k], datay[k])
        if positions.get(t) is None:
            positions[t] = [k]
        else:
            positions.get(t).append(k)


def coupling_2D_axis():
    global granules, dependencies, labels
    granules, dependencies = setup_dependencies_data(sys.argv[1])
    labels = [g.name for g in granules]

    global datax, datay
    datax = [g.getCaArity() for g in granules]
    datay = [g.getCeArity() for g in granules]
    setup_position_structure()

    max_ca_arity = max(g.getCaArity() for g in granules)
    max_ce_arity = max(g.getCeArity() for g in granules)

    fig, axes = plt.subplots(figsize=(6, 6))
    plt.xlim(-1, max_ca_arity + 1)
    axes.yaxis.set_major_locator(MaxNLocator(integer=True))
    plt.ylim(-1, max_ce_arity + 1)

    plt.xlabel("Afferent Coupling (Ca)")
    plt.ylabel("Efferent Coupling (Ce)")
    plt.title("Relationship between Afferent Coupling and Efferent Coupling", y=1)

    granules = plt.scatter(datax, datay)
    mplcursors.cursor(granules, hover=True, highlight=True).connect("add", set_point_hover)

    plt.show()


def set_point_hover(sel):
    index = int(sel.target.index)
    ca, ce = datax[index], datay[index]
    loc = "[Ca = " + str(ca) + "; Ce = " + str(ce) + "]"
    div = ce / (ca + ce) if (ca != 0 or ce != 0) else 0.0
    instability = "[I = Ce / (Ca + Ce) = " + str(round(div, 2)) + "]\n"
    points_on_same_pos = positions[(datax[index], datay[index])]
    granules_display = [labels[i] for i in points_on_same_pos]
    sel.annotation.set_text(loc + " - " + instability + '\n'.join(granules_display))


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Bad CLI args (Require dependency csv file).\nUsage : " + USAGE)
        exit(1)
    coupling_2D_axis()
