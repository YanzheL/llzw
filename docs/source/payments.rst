Payments
********

Payment Entity Definition
=========================

Properties
----------

==================  ========  =====================================================
Parameter           Type      Description
==================  ========  =====================================================
id                  Integer   Payment ID
orderId             Integer   Parent Order ID
createdAt           Date      Creation time
updatedAt           Date      Update time
payerId             String    Username of payer
subject             String    Subject of this payment
description         String    Description
totalAmount         Float     Total amount
status              String    One of ['PENDING', 'CONFIRMED', 'TIMEOUT', 'INVALID']
confirmedAt         Date      Payment confirmation time
confirmed           Boolean   Whether the payment is confirmed by system
valid               Boolean   Valid flag
==================  ========  =====================================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "orderId": 5,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "payerId": "USERNAME_OF_CUSTOMER",
     "subject": "Macbook Pro 2019 32G 1TB",
     "description": null,
     "totalAmount": 500.00,
     "status": "PENDING",
     "confirmedAt": null,
     "confirmed": true,
     "valid": true,
   }

Create a Payment
=================

This endpoint creates a new payment.

HTTP Request
------------

``POST http://example.com/api/v2/payments``

Request Parameters
------------------

==================  ========  ========  =======  =============================
Parameter           Type      Required  Default  Description
==================  ========  ========  =======  =============================
orderId             Integer   True      -        Parent Order ID
subject             String    True      -        Subject of this payment
totalAmount         Float     True      -        Total Amount
description         String    False     -        Description
==================  ========  ========  =======  =============================

Response Parameters
-------------------
=========== ========= ==============================
Parameter   Type      Description
=========== ========= ==============================
data        Payment   The created Payment object
=========== ========= ==============================

.. Attention::
   Remember — You must be authenticated with ``CUSTOMER`` role before using this API

