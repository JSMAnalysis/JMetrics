# Data visualization scripts

In order to exploit data that is extracted from a Java program analysis through JMetrics,
we've provided a set of Python3 scripts.

## Required libraries

To uses this scripts you'll need to install :

* numpy
* matplotlib
* mplcursors

## Available Data

JMetrics lets available to user two kinds of CSV files :

* Metrics data
* Dependencies data

## Usage

The available scripts are :

* stability_histogram.py (use metrics data csv)
* stability_abstraction_2D_axis.py (use metrics data csv)
* dependencies_histogram.py (use dependencies data csv)
* coupling_2D_axis.py (use dependencies data csv)

Note that the stability_abstraction_2D_axis script is an interactive plot.
On point hover, you can visualize the next information:

* position [I, A],
* distance,
* list of granules.

Note that the coupling_2D_axis script is an interactive plot.
On point hover, you can visualize the next information:

* position [Ca, Ce],
* list of granules.

To launch a script, use the command : `python <script_name> <csv_file>`

The dependencies_histogram script require an other argument (the coupling analysis information).
To visualize afferent coupling (respectively efferent coupling); use the argument 'ca' (respectively 'ce').