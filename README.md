## Java kNN

Simple Java implementation of the k-Nearest Neighbours algorithm.


### How to Use

The KNN class needs these fields to make predictions:
 - the path to the <b>training data</b>,
 - the path to the <b>unclassified data</b>,
 - the <b>path where predictions will be written</b>,
 - the <b>column number</b> of the class (starting at 1),
 - <b>k</b> value.


Discrete classed data can be classified with the ``` classify() ``` method. <br>
Continous numerical data can be forecasted with the ``` forecast() ``` method. <br>

### Accuracy Methods

For discrete classed data, accuracy is attained with the static ``` getAccuracy() ``` method, which returns a double from the range [0, 1]. <br>
For continous numerical data, accuracy is attained with the static ``` getRMSE() ``` method, whic returns a double that represents the RMSE.
