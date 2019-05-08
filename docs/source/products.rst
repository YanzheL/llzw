Products
********

Product Entity Definition
=========================

Properties
----------

==================  ========  ==================================================================================================
Parameter           Type      Description
==================  ========  ==================================================================================================
id                  Integer   Product ID
seller              String    Username of seller
name                String    Name of this product
introduction        String    Introduction of this product
mainImageFiles      String[]  Hash values of product main images (max = 9). These files should be uploaded first.
createdAt           Date      Creation time
updatedAt           Date      Update time
price               Float     Price
ca                  String    Certificate authority name
certId              String    Qualification certificate id
caFile              String    Certificate file hash value, this file should be uploaded first, and its type must be image or PDF
valid               Boolean   Valid flag
==================  ========  ==================================================================================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "seller": "USERNAME_OF_SELLER",
     "name": "NAME_OF_THIS_PRODUCT",
     "introduction": "INTRODUCTION_OF_THIS_PRODUCT",
     "mainImageFiles":[
       "1dd4984b0d118569da8620fe67e7fd4bd2889bb316d5ee40ba914eb65f19107d",
       "d0673d4e73e191cc5ba3588f9eac52f7c15e4cba3fc3c229f47ea85d959f97b0"
     ],
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "price": 10.00,
     "ca": "CNAS",
     "certId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
     "caFile": "28e51044f4a9cbae2bbd3d8a9d8c902ad1455d42208277ac4a913b003038a3dc",
     "valid": true
   }

Search Products by Parameters
=============================

This endpoint retrieves all products.

HTTP Request
------------

``GET http://example.com/api/v2/products``

Request Parameters
------------------

================ ======== ======== ======= =============================================================================================
Parameter        Type     Required Default Description
================ ======== ======== ======= =============================================================================================
page             Integer  False    0       The page index from 0
size             Integer  False    20      Page size
valid            Boolean  False    True    Valid flag
name             String   False    -       Name of this product. This field is matched in fuzzy mode
introduction     String   False    -       Introduction of this product. This field is matched in fuzzy mode
ca               String   False    -       Certificate authority name
certId           String   False    -       Qualification certificate id
searchString     String   False    -       Any product that contains this searchString in its name or introduction field will be matched
================ ======== ======== ======= =============================================================================================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Product[] List of matching Product objects
=========== ========= ===================================

Get a Specific Product
======================

This endpoint retrieves a specific product.

HTTP Request
------------

``GET http://example.com/api/v2/products/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Product ID
========= ======== ===========

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Product   The matching Product object
=========== ========= ===================================

Create a Product
================

This endpoint creates a new product.

HTTP Request
------------

``POST http://example.com/api/v2/products``

Request Parameters
------------------

================ ======== ======== ======= ================================================================================================
Parameter        Type     Required Default Description
================ ======== ======== ======= ================================================================================================
name             String   True     -       Name of this product
introduction     String   True     -       Introduction of this product
price            Float    True     -       Price of this product
ca               String   True     -       Certificate authority name
certId           String   True     -       Qualification certificate id
caFile           String   True     -       Hash of uploaded CA file
mainImageFiles   String[] False    -       Main image URLs for this product, which can be the HASH value of uploaded images. Max length = 9
================ ======== ======== ======= ================================================================================================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Product   The created Product object
=========== ========= ===================================

.. Attention::
   Remember — You must be authenticated with ``SELLER`` role before using this API

Update a Specific Product
=========================

This endpoint updates infomation of a specific product.

HTTP Request
------------

``PATCH http://example.com/api/v2/products/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Product ID
========= ======== ===========

Request Parameters
------------------

================ ======== ======== ======= ================================================================================================
Parameter        Type     Required Default Description
================ ======== ======== ======= ================================================================================================
name             String   False    -       Name of this product
introduction     String   False    -       Introduction of this product
price            Float    False    -       Price of this product
ca               String   False    -       Certificate authority name
certId           String   False    -       Qualification certificate id
caFile           String   False    -       Hash of uploaded CA file
mainImageFiles   String[] False    -       Main image URLs for this product, which can be the HASH value of uploaded images. Max length = 9
================ ======== ======== ======= ================================================================================================

.. Attention::
   Remember — You must be authenticated with ``SELLER`` role before using this API

Invalidate a Specific Product
=============================

This endpoint invalidates a specific product, so all stocks of this
product will no longer be in the queue for sale.

It will NOT delete it from database.

HTTP Request
------------

``DELETE http://example.com/api/v2/products/<ID>``

Path Parameter
--------------

========= ======== ===========
Parameter Required Description
========= ======== ===========
ID        True     Product ID
========= ======== ===========

.. Attention::
   Remember — You must be authenticated with ``SELLER`` role before using this API
