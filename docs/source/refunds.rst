Refunds
*******

Refund Entity Definition
========================

Properties
----------

==================  ========  =========================================================================
    Parameter       Type      Description
==================  ========  =========================================================================
id                  Integer   Refund ID
orderId            Integer   Order ID
createdAt           Date      Creation time
updatedAt           Date      Update time
requester           String    Username of requester
requestReason       String    Reason of requester
responseReason      String    Reason of responder. e.g. The reason of rejection.
confirmedAt         Date      Refund confirm time
issuedAt            Date      Refund issue time
amount              Float     amount
status              String    status, one of ['PROCESSING', 'DENIED', 'ISSUED', 'CANCELLED', 'PENDING']
valid               Boolean   Valid flag
==================  ========  =========================================================================

Example JSON Representation
---------------------------

.. code:: json

   {
     "id": 1,
     "orderId": 5,
     "createdAt": "2019-10-1 3:00 PM GMT+1:00",
     "updatedAt": "2019-10-1 3:00 PM GMT+1:00",
     "requester": "USERNAME_OF_REFUND_ISSUER",
     "requestReason": "I hate this product",
     "responseReason": null,
     "confirmedAt": null,
     "issuedAt": null,
     "amount": 500.0,
     "status": "REQUESTED",
     "valid": true
   }

Get Refunds Infomation
======================

This endpoint gets infomation of refunds.

HTTP Request
------------

``GET http://example.com/api/v2/refunds``

Request Parameters
------------------

=========== ======= ======== ======= =========================================================================
Parameter   Type    Required Default Description
=========== ======= ======== ======= =========================================================================
page        Integer False    0       The page index from 0
size        Integer False    20      Page size
orderId    String  False    -       Order ID
status      String  False    -       status, one of ['PROCESSING', 'DENIED', 'ISSUED', 'CANCELLED', 'PENDING']
valid       Boolean False    -       Valid flag
=========== ======= ======== ======= =========================================================================

Response Parameters
-------------------
=========== ======== ===============================
Parameter   Type     Description
=========== ======== ===============================
data        Refund[] List of matching Refund objects
=========== ======== ===============================


..  Attention::
    The requested orderId (if provided) must belong to you.

Create a Refund
===============

This endpoint creates a new refund.

HTTP Request
------------

``POST http://example.com/api/v2/refunds``

Request Parameters
------------------

==================  ========  ========  =======  ===============================================
    Parameter        Type     Required  Default  Description
==================  ========  ========  =======  ===============================================
orderId            Integer   True      -        ID of the order it belongs to
reason              String    True      -        Reason of this refund
amount              Float     True      -        Refund amount
==================  ========  ========  =======  ===============================================

Response Parameters
-------------------
=========== ======== ===================================
Parameter   Type     Description
=========== ======== ===================================
data        Refund   The created Refund object
=========== ======== ===================================

..  Attention::
    Remember — You must be authenticated with ``SELLER`` or ``CUSTOMER`` role before using this API
