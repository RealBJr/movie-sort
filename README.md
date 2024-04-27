# movie-sort
## Situation
Mr. Filmbuff created a number of text files, each storing information about movies released in the same year.<br>
Each text file contains zero or more lines separated by a newline character. Each line represents a movie record, specifying information about 10 movie features, namely:
<br><b>year, title, duration, genres, rating, score, director, actor 1, actor 2, and actor 3.</b><br>
With a movie catalog containing several hundred of entries, Mr. Filmbuff finds it a bit tedious to
read through his densely formatted text files and a bit frustrating when he finds typos in his catalog.
Turning to you for help, he would like you to
<ol>
  <li>
     partition all valid movie records into new genre-based1
  text files,
  </li>
  <li>
    load an array of movie records from each of the partitioned text file, serializing the resulting
    movie array to a binary file, and
  </li>
  <li>
    deserialize (reconstruct) the serialized arrays from the binary files into a 2D-array of movie
    record objects, and finally provide an interactive program that allows the user to navigate a
    movie array, displaying user-specified number of movie-records.
  </li>
</ol>

## Learning Outcomes
The purpose of this project is to allow me to practice Exception Handling and File I/O as well as other object-oriented concepts
