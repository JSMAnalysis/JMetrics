#!/usr/bin/env python3

'''
    Python script that generate horizontal histogram of a given component's metrics from a csv file.
'''
USAGE = "python stability_histogram.py <csv_file> <Ca|Ce|A|I|Dn>"
components = {
    "Ca": "Afferent coupling",
    "Ce": "Efferent coupling",
    "A":  "Abstraction",
    "I":  "Instability",
    "Dn": "Distance"
}


from retrieve_csv_data import retrieve_data
import matplotlib.pyplot as plt
import sys


def setup_data():
    csv = retrieve_data(sys.argv[1], ("Granule", sys.argv[2]))
    csv = sorted(csv, key=lambda x: x[1])
    labels = []
    datax = []
    datay = []
    for i in range(len(csv)):
        labels.append(csv[i][0])
        datax.append(i)
        datay.append(csv[i][1])
    return labels, datax, datay


def metrics_histogram():
    labels, datax, datay = setup_data()
    plt.figure(figsize=(20, 0.5 * len(datax)))
    plt.axes([0.3, 0.1, 0.6, 0.8])
    plt.barh(datax, datay, align='center')
    plt.yticks(datax, labels)
    componentName = components[sys.argv[2]]
    plt.xlabel(componentName)
    plt.title(componentName + " by granule")
    plt.show()


if __name__ == "__main__":
    if len(sys.argv) != 3 or sys.argv[2] not in components.keys():
        print("Bad CLI args (Require martinMetrics csv file).\nUsage : " + USAGE)
        exit(1)
    metrics_histogram()
