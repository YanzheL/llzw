Products
********

Product Entity Definition
=========================

Properties
----------

==================  ========  ============================
Parameter           Type      Description
==================  ========  ============================
id                  Integer   Order ID
sellerId            String    Quantity of this order
name                String    Name of this product
introduction        String    Introduction of this product
createdAt           Date      Creation time
updatedAt           Date      Update time
price               Float     Price
ca                  String    Certificate authority name
certId              String    Qualification certificate id
valid               Boolean   Valid flag
==================  ========  ============================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "sellerId": "USERNAME_OF_SELLER",
     "name": "NAME_OF_THIS_PRODUCT",
     "introduction": "INTRODUCTION_OF_THIS_PRODUCT",
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "price": 10.00,
     "ca": "CNAS",
     "certId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
     "valid": true
   }

Get All Products
================

This endpoint retrieves all products.

HTTP Request
------------

``GET http://example.com/api/v2/products``

Request Parameters
------------------

========= ======= ======== ======= =====================
Parameter Type    Required Default Description
========= ======= ======== ======= =====================
page      Integer False    0       The page index from 0
size      Integer False    20      Page size
valid     Boolean False    True    Valid flag
========= ======= ======== ======= =====================

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

Request Parameters
------------------

========= ======= ======== ======= =================================
Parameter Type    Required Default Description
========= ======= ======== ======= =================================
ID        Integer True     -       The ID of the product to retrieve
========= ======= ======== ======= =================================

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

================ ======= ======== ======= ==========================================
Parameter        Type    Required Default Description
================ ======= ======== ======= ==========================================
name             String  True     -       Name of this product
introduction     String  True     -       Introduction of this product
price            Float   True     -       Price of this product
ca               String  True     -       Certificate authority name
certId           String  True     -       Qualification certificate id
================ ======= ======== ======= ==========================================

Response Parameters
-------------------
=========== ========= ===================================
Parameter   Type      Description
=========== ========= ===================================
data        Product   The created Product object
=========== ========= ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API

Invalidate a Specific Product
=============================

This endpoint invalidates a specific product, so all stocks of this
product will no longer be in the queue for sale.

It will NOT delete it from database.

HTTP Request
------------

``DELETE http://example.com/api/v2/products/<ID>``

Request Parameters
------------------

========= ===================================
Parameter Description
========= ===================================
ID        The ID of the product to invalidate
========= ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` role before using this API
