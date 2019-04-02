#!/usr/bin/env python3

'''
    Python script that computes statistical values on the metrics
'''
USAGE = "python statistics.py <csv_file>"


from retrieve_csv_data import retrieve_data
import numpy as np
import sys


def setup_data():
    csv = retrieve_data(sys.argv[1], ("Ce", "Ca", "A", "I", "Dn"))
    ce, ca, a, i, dn = [], [], [], [], []
    for metrics in csv:
        ce.append(metrics[0])
        ca.append(metrics[1])
        a.append(metrics[2])
        i.append(metrics[3])
        dn.append(metrics[4])
    return ce, ca, a, i, dn


def print_components(ce, ca, a, i, dn, fun, fun_name):
    print(fun_name + " values per metric component :")
    print("\tCe : " + str(round(fun(ce), 2)))
    print("\tCa : " + str(round(fun(ca), 2)))
    print("\tA : " + str(round(fun(a), 2)))
    print("\tI : " + str(round(fun(i), 2)))
    print("\tDn : " + str(round(fun(dn), 2)))


def compute_statistics():
    ce, ca, a, i, dn = setup_data()
    print_components(ce, ca, a, i, dn, np.mean, "Mean")
    print_components(ce, ca, a, i, dn, np.median, "Median")
    print_components(ce, ca, a, i, dn, np.var, "Variance")
    print_components(ce, ca, a, i, dn, np.std, "Standard deviation")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Bad CLI args (Require martinMetrics csv file).\nUsage : " + USAGE)
        exit(1)
    compute_statistics()
