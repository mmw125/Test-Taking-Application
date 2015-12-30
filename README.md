# Test-Taking-Application
This is an application teachers can use to give tests and other assignments to students.

#Overview
This program allows teachers to login, create assignments with an answer key, upload them to a server, 
have students take them, submit their answers, receive an instant grade, and then have the teacher be able to see the grade too. Currently this only supports multiple choice questions.

#Configuring
This program needs to be connected to an FTP server. That server needs to have a data.txt file in the root that defines 
teacher names, user names, email, and hashed and salted password. Same for the students. Then you need to define the classes 
by having the name of the class and the user name of the teacher that teaches the class followed by the students in the class.

Then each teacher needs to have a folder on the server for their user name, and one under that for each class they teach.
Folders will be created under them for each assignment in that class.

The specifics for server address, user name, password, and port are defined in the config/serverConfig.txt file

Use src/utilities/passwordHash to create hashed and salted passwords for the data file.

#Contact Me
If you have any comments, questions, or concerns, email me at mmw125@gmail.com