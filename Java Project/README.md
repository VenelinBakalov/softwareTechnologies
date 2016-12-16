# newsWebsiteProject
A SoftUni Software Technologies project developed for learning

Project workflow:

1. Added date to Article on creation
2. Created Position Entity with the following functionality
 -> Create, Edit, Delete;
 -> Show author position on Article details view;
 -> Added user position on User/Edit;
 -> When admin deletes a position all users that had this position are set to "Guest" position before it's deletion;
 -> When admin tries to edit or delete "Guest" position he is redirected to position list as "Guest" is the default position and cannot be deleted
3. Added "Our team" page - still work to be done on that view
4. Created "My Articles" view from My profile page, listing all user articles in a table with options to edit/delete. Also when accessing My articles check is made on wether the logged user Id equals the one from the url (the path variable).
5. Created Video Entity with all relations and create functionality. More details to be added in later commits.
6. Added My videos page ("list-videos.html) and sorted articles in My Articles and videos in My Videos both by id. Also, added more info about video in Article details.
7. Added Edit and Delete Video functionality. Check is made wether the user is admin or author of the video (with an override of isAuthor() method - isAuthor(Video video).
8. Home page is editted to show latest article and a sidebar with latest five articles.
9. Added functionality for editing password in my profile page.
10. Added profile picture upload, "Add/change Profile Picture" button to My profile page.
11. Added comments functionality - Comment Entity, Edit and Delete funcionality.
