Authentication
**************

This endpoint performs login action. After a successful login, the server will send you an authenticated COOKIE which represents all your privileges.

All your subsequent requests will inherit this identity.

HTTP Request
------------

``POST http://example.com/login``

Request Parameters
------------------

========= ====== ======== ======= =============
Parameter Type   Required Default Description
========= ====== ======== ======= =============
username  String True     -       Your username
password  String True     -       Your password
========= ====== ======== ======= =============

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": null
   }
