#!/usr/bin/env python3

'''
    Python script to generate a 2D axis of the stability and abstraction from a csv file.
    usage : python stability_histogram.py <csv_file_name>
'''

from retrieve_csv_data import retrieve_data
import matplotlib.pyplot as plt
import sys
import mplcursors



def setupData():
    csv = retrieve_data(sys.argv[1], ("Granule", "I", "A", "Dn"))
    granules = []
    instability = []
    asbstractness = []
    distance = []
    for i in range(len(csv)):
        granules.append(csv[i][0])
        instability.append(csv[i][1])
        asbstractness.append(csv[i][2])
        distance.append(csv[i][3])
    return granules, instability, asbstractness, distance



def setupPositionStructure():
    ''' Creates an (average time complexity) O(1) granules access structure.
    Dict with tuple (i, a) as key; granule index as value) '''
    global positions
    positions = dict()
    for k in range(len(labels)):
        tuple = (datax[k], datay[k])
        if positions.get(tuple) == None:
            positions[tuple] = [k]
        else:
            positions.get(tuple).append(k)



def instability_abstraction_2D_axis():
    global labels, datax, datay, distance
    labels, datax, datay, distance = setupData()
    setupPositionStructure()

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
    plt.xlabel("Abstraction")
    plt.ylabel("Instability")
    plt.text(0.52, 0.5, "Main Sequence", rotation=-45, horizontalalignment='center', verticalalignment='center')
    plt.title("Instability and Abstraction on 2D axis", y=1)
    mplcursors.cursor(granules, hover=True, highlight=True).connect("add", setPointHover)

    plt.show()



def setPointHover(sel):
    index = int(sel.target.index)
    loc = "[I = " + str(datax[index]) + "; A = " + str(datay[index]) + "]"
    dist = "Distance = " + str(distance[index])
    pointsOnSamePos = positions[(datax[index], datay[index])]
    granulesDisplay = [labels[g] for g in pointsOnSamePos]
    sel.annotation.set_text(loc + " - " + dist + "\n" + '\n'.join(granulesDisplay))



if __name__ == "__main__":
    instability_abstraction_2D_axis()
