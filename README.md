## Source & Test Code

Source Code: "https://github.com/BrenKenna/JavaBase/tree/main/CustomDatabase/lib/src/main/java"
<br>
Unit Tests: "https://github.com/BrenKenna/JavaBase/tree/main/CustomDatabase/lib/src/test/java"
<br>

## Overview

Native Java Database library developed using Reflection. The library models a ***Database*** as a collection of ***Tables*** (Map- Table Name, Table), a Table is a concrete ***Dataset*** which is a List of ***Datapoints***. Where faster log(n) query times is supported for a ***Table***, which have a collection of ***BinarySearchTrees*** (Map- String, BinarySearchTree) allowing the fields of a *Datapoint* to be queried.

The core system model is a *Datapoint* where a users Model class (ie Person, Square), can be stored into & retrieved from using Reflection. Such operations are performed by a ***ModelDatapointConverter*** of type Model class (i.e one per Table of Model class). With the *Datapoint* only needing to know the referenced class name, and the client havingthe appropriate Model class in its Class Path. Akin to the *POJOCodecProvider* for a *Document* in MongoDB.

A *Datapoint* is a collection of ***ModelAttributes*** (Map- String, *ModelAttribute*), allowing the data to be stored to change if needs be, on the Datapoint level. This is because a ***ModelAttribute***, stores the field name of a Class, its alias, its *DataType* and value (ex "fName" for the "First Name" of a Person Model class)

The *DataType* is managed ***Strategically*** by a "***DataTypeEnum***" of valid primitive types (ie String, int, double, char, boolean). Where a specific *DataTypeEnum* value, such as String, Int, can measure the numerical distance between two types, or throw a ***DataTypeException*** if inputs are of different types (ex String, and a Boolean).

Since Model classes can be converted into a Datapoint, and stored in an ***Abstract Dataset*** (concretes are *Table*, *Results*). A ***BinarySearchTree*** can be built from the *DataType*, Value of a *ModelAttribute* (akin to a column), and its position in the Datapoint ArrayList property of a Table. Facilitating faster log linear query times on a Table, over linear scan operations on Datapoints. Additionally, each node (***IndexEntry***) of a Datapoint has a bucket containing Duplicates, allowing a Table to contain multiple occurences of a Persons First Name.

A *Datapoint* having a ***DatapointState*** enum of either *Active*, or *Dropped*. Enables the rows of a Table protection against accidental deletions. As the state can be changed on the ***Datapoint*** level. It also simplifies *Index* management, because rows in a *Dropped* state can be hidden from the client-side view following queries which return a *Results-Dataset* .

*DatapointStates* also facilitates versioning tables, where Dropped rows can be collected at anytime, and provided as *ResultsDataset*. In time, can work with Persistence packages for Database Chores.
<br>

## Development State

- Core packages developed and tested. Output from JUnit tests provided as a text file in this repo, see "*Package-Test-Results.txt*".

- Updating of fields (method input of field, new value, overloaded method using index in array) of rows can be revisited with the recent addition of a DatapointState.

- Persistence in the form of gzipped tables can use code from "*[BrenKenna-JsonPersistence](https://github.com/BrenKenna/JsonPersistence)*"

- A parallel Stream can be used to create the Map-String, BinarySearchTree used to construct the Index for a Table (continue to note other oppurtunities).

- Model subclasses not tested.

- With the project structure in place can look at the use of internal IDs (UUID, and timestamp), noting additional considerations for "Database/Table Chores".


## Notes from Testing

- Querying a table of 50k datapoints, into a results set of ~2.5k takes 1.1s. Which is very good performance, and is also seen with "DELETE * MATCHING", then "SELECT * MATCHING OTHER" test. Which adds two queries, dropping of thousands of records, and re-indexing the table. 

- While the construction of ~5M Model classes takes ~3.35, their conversion to Datapoints froze my local computer.

- Highlights interesting point of roles given that reflection is expensive, as Database will never be doing this (i.e will only ever know what a Datapoint is). 

- Only clients, or a REST-API, will convert a Model class to a Datapoint, and vice versa. Where the REST-API will also have to battle the incoming traffic. Independantly these actions maybe fine, but maybe not together in a high traffic environment (testing separately in "*[BrenKenna-DevOps](https://github.com/BrenKenna/DevOps-CCT)*").

- What is also interesting, is that similar action will be taken even with more optimized libs like mongos POJO codec.

- Can also imagine that these actions could occur alongside say "SELECT *" queries.

- Where in a high traffic evironment, it would be useful to separate the concerns of the "Write Path" and the "Read Path".