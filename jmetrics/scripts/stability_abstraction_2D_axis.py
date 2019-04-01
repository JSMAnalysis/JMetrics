#!/usr/bin/env python3

'''
    Python script to generate a 2D axis plot of the stability and abstraction from a csv file.
'''
USAGE = "python stability_histogram.py <csv_file>"


from retrieve_csv_data import retrieve_data
import matplotlib.pyplot as plt
import sys
import mplcursors


def setup_data():
    csv = retrieve_data(sys.argv[1], ("Granule", "I", "A", "Dn"))
    granules = []
    instability = []
    abstractness = []
    distance = []
    for i in range(len(csv)):
        granules.append(csv[i][0])
        instability.append(csv[i][1])
        abstractness.append(csv[i][2])
        distance.append(csv[i][3])
    return granules, instability, abstractness, distance


def setup_position_structure():
    ''' Creates an (average time complexity) O(1) granules access structure.
    Dict with tuple (i, a) as key; granule index as value) '''
    global positions
    positions = dict()
    for k in range(len(labels)):
        t = (datax[k], datay[k])
        if positions.get(t) is None:
            positions[t] = [k]
        else:
            positions.get(t).append(k)


def instability_abstraction_2D_axis():
    global labels, datax, datay, distance
    labels, datax, datay, distance = setup_data()
    setup_position_structure()

    fig, axes = plt.subplots(figsize=(6, 6))
    plt.axes().set_aspect('equal', 'datalim') # Keep square ratio

    # Plot
    pain_zone = plt.Circle((0, 0), 0.4, color='r', fill=False)
    uselessness_zone = plt.Circle((1, 1), 0.4, color='g', fill=False)
    main_sequence = plt.plot([1, 0], [0, 1], color="black", linestyle="solid")
    granules = plt.scatter(datax, datay)
    plt.gcf().gca().add_artist(pain_zone)
    plt.gcf().gca().add_artist(uselessness_zone)

    # Details
    plt.legend(
        [pain_zone, uselessness_zone, granules],
        ["Zone of Pain", "Zone of Uselessness", "Granule"],
        loc=9, bbox_to_anchor=(0.5, 1.15), shadow=True, ncol=3
    )
    plt.axis([0, 1, 0, 1])
    plt.xlabel("Instability")
    plt.ylabel("Abstractness")
    plt.text(0, -0.1, 'stable', horizontalalignment='left', verticalalignment='bottom')
    plt.text(1, -0.1, 'unstable', horizontalalignment='right', verticalalignment='bottom')
    plt.text(-0.15, 0, 'concrete', horizontalalignment='left', verticalalignment='bottom', rotation=90)
    plt.text(-0.15, 1, 'abstract', horizontalalignment='left', verticalalignment='top', rotation=90)

    plt.text(0.52, 0.5, "Main Sequence", rotation=-45, horizontalalignment='center', verticalalignment='center')
    plt.title("Instability and Abstraction on 2D axis", y=1)
    mplcursors.cursor(granules, hover=True, highlight=True).connect("add", set_point_hover)

    plt.show()


def set_point_hover(sel):
    index = int(sel.target.index)
    loc = "[I = " + str(datax[index]) + "; A = " + str(datay[index]) + "]"
    dist = "Distance = " + str(distance[index])
    points_on_same_pos = positions[(datax[index], datay[index])]
    granules_display = [labels[g] for g in points_on_same_pos]
    sel.annotation.set_text(loc + " - " + dist + "\n" + '\n'.join(granules_display))


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Bad CLI args (Require martinMetrics csv file).\nUsage : " + USAGE)
        exit(1)
    instability_abstraction_2D_axis()
