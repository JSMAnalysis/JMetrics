#!/usr/bin/env python3

'''
    Python script to generate a 2D axis of the stability and abstraction from a csv file.
    usage : python stability_histogram.py <csv_file_name>
'''

import numpy as np
import matplotlib.pyplot as plt
import sys


def retrieve_data(usecols):
    return np.genfromtxt(sys.argv[1], delimiter=';', dtype=None, encoding=True, names=True, usecols=usecols)

def instability_abstraction_2D_axis():
    # Retrieve data
    csv = retrieve_data(("Granule", "I", "A"))
    labels = []
    datax = []
    datay = []
    for i in range(len(csv)):
        labels.append(csv[i][0])
        datax.append(csv[i][1])
        datay.append(csv[i][2])

    return labels, datax, datay

fig,axes = plt.subplots(figsize=(10,10))
labels, datax, datay = instability_abstraction_2D_axis()

# Plot
pain_zone = plt.Circle((0, 0), 0.4, color='r', fill=False)
useless_zone = plt.Circle((1, 1), 0.4, color='g', fill=False)
plt.gcf().gca().add_artist(pain_zone)
plt.gcf().gca().add_artist(useless_zone)
plt.text(0.52, 0.5, "Main Sequence", rotation=-45, horizontalalignment='center', verticalalignment='center')
main_sequence = plt.plot([1, 0], [0, 1], color="black", linestyle="solid")
granule = plt.scatter(datax, datay)

plt.legend(
    [pain_zone, useless_zone, granule],
    ["Zone of Pain", "Zone of Useless", "Granule"],
    loc=9, bbox_to_anchor=(0.5, 1.14), fancybox=True, shadow=True, ncol=3
)
plt.axis([0, 1, 0, 1])
plt.xlabel("Abstraction")
plt.ylabel("Instability")
plt.title("Instability and Abstraction on 2D axis", y = 1.05)

fileName = axes.annotate("", xy=(0,0), xytext=(10,10),textcoords="offset pixels",
                    bbox=dict(boxstyle="round", fc="w"),
                    arrowprops=dict(arrowstyle="->"))
fileName.set_visible(False)

def printLabel(index):
    pos = granule.get_offsets()[index["ind"][0]]
    fileName.xy = pos

    text = "{}".format(" \n".join([labels[n] for n in index["ind"]]))
    fileName.set_text(text)
    fileName.get_bbox_patch().set_alpha(0.4)

def onClickEvent(event):
    visible = fileName.get_visible()
    if event.inaxes == axes:
        cont, ind = granule.contains(event)
        if cont:
            printLabel(ind)
            fileName.set_visible(True)
            fig.canvas.draw_idle()
        else:
            if visible:
                fileName.set_visible(False)
                fig.canvas.draw_idle()


fig.canvas.mpl_connect("button_press_event", onClickEvent)
plt.show()


if __name__ == "__main__":
    instability_abstraction_2D_axis()