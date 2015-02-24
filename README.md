# Test-Taking-Application
This is an application teachers can use to give tests and other assignments to students.

#Abandoned


#Overview
This program allows teachers to login, create assignments with an answer key, upload them to a server, 
have students take them, submit their answers, receive an instant grade, and then have the teacher be able to see the grade too. Currently this only supports multiple choice questions.

#Configuring
This program needs to be connected to an ftp server. That server needs to have a data.txt file in the root that defines 
teacher names, usernames, email, and hashed and salted password. Same for the sudents. Then you need to define the classes 
by having the name of the class and the username of the teacher that teaches the class followed by the students in the class.

Then each teacher needs to have a folder on the server for their username, and one under that for each class they teach.
Folders will be created under them for each assignment in that class.

You will need to change the variables in src/ftp/FTPConnection.connectToServer() to the values needed to connect to the server.

Use src/utilities/passwordHash to create hashed and salted passwords for the data file.

#Contact Me
If you have any comments, questions, or conserns, email me at mmw125@gmail.com
