Products
********

Get All Products
================

This endpoint retrieves all products.

HTTP Request
------------

``GET http://example.com/api/v1/products``

Request Parameters
------------------

========= ======= ======== ======= =====================
Parameter Type    Required Default Description
========= ======= ======== ======= =====================
page      Integer False    0       The page index from 0
size      Integer False    20      Page size
========= ======= ======== ======= =====================

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": [
       {
         "id": 1,
         "valid": true,
         "seller_id": "USERNAME_OF_SELLER",
         "name": "NAME_OF_THIS_PRODUCT",
         "introduction": "INTRODUCTION_OF_THIS_PRODUCT",
         "createdAt": "2019-10-1 3:00 PM GMT+1:00",
         "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
         "price": 10.00,
         "maxDeliveryHours": 72,
         "ca": "CNAS",
         "certId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
       },
       {
         "id": 2,
         "valid": true,
         "seller_id": "USERNAME_OF_SELLER",
         "name": "NAME_OF_THIS_PRODUCT",
         "introduction": "INTRODUCTION_OF_THIS_PRODUCT",
         "createdAt": "2019-10-1 3:00 PM GMT+1:00",
         "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
         "price": 10.00,
         "maxDeliveryHours": 72,
         "ca": "CNAS",
         "certId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
       }
     ]
   }

Get a Specific Product
======================

This endpoint retrieves a specific product.

HTTP Request
------------

``GET http://example.com/api/v1/products/<ID>``

Request Parameters
------------------

========= ======= ======== ======= =================================
Parameter Type    Required Default Description
========= ======= ======== ======= =================================
ID        Integer True     -       The ID of the product to retrieve
========= ======= ======== ======= =================================

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "valid": true,
       "seller_id": "USERNAME_OF_SELLER",
       "name": "NAME_OF_THIS_PRODUCT",
       "introduction": "INTRODUCTION_OF_THIS_PRODUCT",
       "createdAt": "2019-10-1 3:00 PM GMT+1:00",
       "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
       "price": 10.00,
       "maxDeliveryHours": 72,
       "ca": "CNAS",
       "certId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     }
   }

Create a Product
================

This endpoint creates a new product.

HTTP Request
------------

``POST http://example.com/api/v1/products``

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
maxDeliveryHours Integer False    72      Max delivery hours allowed of this product
================ ======= ======== ======= ==========================================

..  Attention::
    Remember — You must be authenticated with SELLER role before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": {
       "id": 1,
       "valid": true,
       "seller_id": "USERNAME_OF_SELLER",
       "name": "NAME_OF_THIS_PRODUCT",
       "introduction": "INTRODUCTION_OF_THIS_PRODUCT",
       "createdAt": "2019-10-1 3:00 PM GMT+1:00",
       "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
       "price": 10.00,
       "maxDeliveryHours": 72,
       "ca": "CNAS",
       "certId": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     }
   }

Invalidate a Specific Product
=============================

This endpoint invalidates a specific product, so all stocks of this
product will no longer be in the queue for sale.

It will NOT delete it from database.

HTTP Request
------------

``DELETE http://example.com/api/v1/products/<ID>``

Request Parameters
------------------

========= ===================================
Parameter Description
========= ===================================
ID        The ID of the product to invalidate
========= ===================================

..  Attention::
    Remember — You must be authenticated with SELLER role before using this API

The response JSON structured like this:

.. code:: json

   {
     "responseId": "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
     "sucess": true,
     "data": null
   }
