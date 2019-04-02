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

* metrics_histogram.py (use martinMetrics data csv)
* stability_abstraction_2D_axis.py (use martinMetrics data csv)
* dependencies_histogram.py (use dependencies data csv)
* coupling_2D_axis.py (use dependencies data csv)

Note that stability_abstraction_2D_axis and coupling_2D_axis scripts are interactive plot.  
(stability_abstraction_2D_axis) On point hover, you can visualize the next information:

* position [I, A],
* distance,
* list of granules.

(coupling_2D_axis) On point hover, you can visualize the next information:

* position [Ca, Ce],
* instability value,
* list of granules.

To launch a script, use the command : `python <script_name> <csv_file>`  
The dependencies_histogram script require an other argument (the coupling analysis information).
To visualize afferent coupling (respectively efferent coupling); use the argument 'ca' (respectively 'ce').  

The metrics_histogram script require an other argument (the metrics component to visualize).
Give an argument in <Ca|Ce|I|A|Dn> for visualizing the corresponding component's histogram.
