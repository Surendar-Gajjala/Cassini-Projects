<html>
<head>
    <link rel="stylesheet" type="text/css" href="/app/assets/css/style.default.css">
    <link rel="stylesheet" type="text/css" href="/app/assets/css/style.katniss.css">

    <title>Cassini Systems</title>

    <style type="text/css">
        .page-break {
            display: none;
        }

        body {
            background-color: #F2F7F9 !important;
            font-size: 12px !important;
        }

        .contentPanel {
            padding: 30px;
            background-color: #FFF !important;
            border: 1px solid #D0DDE1
        }

        @media screen and (min-width: 801px)  {
            .content {
                margin-right: auto;
                margin-left: auto;
                width: 60%;
                margin-top: 50px;
                margin-bottom: 50px;
            }
        }

        @media screen and (max-width: 800px) {
            body {
                background-color: #FFF;
                padding: 0px;
                font-size: 12px !important;
            }

            .page-break {
                display: block;
                page-break-before: always;
            }

            .content {
                margin-right: 0px;
                margin-left: 0px;
                width: 100%;
            }

            .contentPanel {
                padding: 00px;
                background-color: #FFF !important;
                border: 0;
            }

            .break-line {
                display: none;
            }
        }

        @media print {
            body {
                background-color: #FFF !important;
                padding: 0px;
                font-size: 12px !important;
            }

            .page-break {
                display: block;
                page-break-before: always;
            }

            .content {
                margin-right: 0px;
                margin-left: 0px;
                width: 100%;
            }

            .contentPanel {
                padding: 00px;
                background-color: #FFF !important;
                border: 0;
            }

            .break-line {
                display: none;
            }

            td, th {
                font-size: 12px !important;
                text-align: center;
                background-color: #FFF;
            }

            p {
                font-size: 12px;
            }
        }

        td, th {
            text-align: center;
            background-color: #FFF;
            padding: 0 !important;
        }

        td {
            font-size: 12px !important;
        }

        h1,h2,h3,h4,h5,p {
            margin: 0 !important;
        }

    </style>
</head>

<%
    def date = new Date().format("dd-MM-yyyy");
    def pattern = "##,###.##";
    def moneyform = new java.text.DecimalFormat(pattern);
    String value = moneyform.format(consignment.value);

    String paid = "TO-PAY";
    if (consignment.paidBy == com.cassinisys.erp.model.crm.PaidBy.SELF) {
        paid = "PAID";
    }

    String deliveryMode = "WITH PASS";
    if (consignment.deliveryMode == com.cassinisys.erp.model.crm.DeliveryMode.DOOR_DELIVERY) {
        deliveryMode = "DOOR DELIVERY";
    } else if (consignment.deliveryMode == com.cassinisys.erp.model.crm.DeliveryMode.ORDINARY) {
        deliveryMode = "ORDINARY";
    }
    String invoiceNumber = "";
    if (consignment.shipments != null && consignment.shipments.size() > 0) {
        def size = consignment.shipments.size();
        consignment.shipments.eachWithIndex { item, idx ->
                invoiceNumber += item.invoiceNumber;
                if(idx != size-1) {
                    invoiceNumber += ",";
                }
        }
    }

%>
<body>
    <div class="content contentPanel">
        <div>
            <div class="row">
                <div class="col-xs-8">
                    <h5><strong>DACHEPALLI PUBLISHERS(P) LTD</strong></h5>
                    <h5><strong>Educational Publishers & Book sellers</strong></h5>

                    <p>Plot No.2/B,(C.F.Area) I.D.A Cherlapalli,Phase 2,</p>

                    <p>Hyderabad-51</p>

                    <p>E-mail:vinod@dachepalli.com,</p>

                    <p>Visit us at:www.dachepalli.com</p>

                </div>

                <div class="col-xs-4">
                    <p>Ph:040-64579788,65220941,65220942</p><br>

                    <div>
                        <table class="table table-bordered">
                            <tbody>
                            <tr>
                                <td><strong>VAT TIN NO:36160188951</strong></td>
                            </tr>
                            <tr>
                                <td><strong>CST:SEC/10/1/2235/97-98</strong></td>
                            </tr>

                            <tr>
                                <td><strong>CIN-U22110AP1998PTC028994</strong></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <h5 class="text-right"><strong>Date: $date</strong></h5>
                    </div>
                </div>

            </div>
            <div class="row">
                <div class="col-md-12">
                    <h5 class="text-center"><strong style="border-bottom: 1px solid #ddd;">T.S.G.S.T RULES</strong></h5>

                    <p>Rules 45(4) Clauses to permit thereunder.Provided further that it shall not necessary for the goods specified in the fourth
                        Schedule to the act to be accompanied by way bills issued under the authority of State Government of the commissioner tax.
                    </p>
                </div>
            </div>
            <div class="row" style="line-height: 15px;">
                <div class="col-md-12">
                    <h5 class="text-center"><strong style="border-bottom: 1px solid #ddd;">DECLARATION</strong></h5>

                    <P>WE DO HERE BY DECLARE that the consignment booked under PAID or TO-PAY parcel</P>

                    <div class="row">
                        <div class="col-sm-4">
                            <p>Way Bill No: <strong>$invoiceNumber</strong> </p>
                        </div>

                        <div class="col-sm-8">
                            Dated: $date Of <strong>$consignment.contents PRINTED BOOKS GUNNY BUNDLES</strong>
                        </div>
                    </div>
                    <P>Approximately valued of <strong>&#8377;$value</strong> are printed books which are exempted from payment of sales Tax.</P>
                </div>
            </div>
            <div class="row" style="line-height: 15px;">
                <div class="col-xs-6">
                    <p><strong>Name and address of the consignee</strong></p>

                    <strong><p style="border-bottom: 1px solid #ddd; font-size:15px">$consignment.consignee</p></strong>

                    <p style="border-bottom: 1px solid #ddd;font-size:14px">$consignment.shippingAddress.addressText</p>

                    <p style="border-bottom: 1px solid #ddd;font-size:14px">
                        $consignment.shippingAddress.city, $consignment.shippingAddress.state.name - $consignment.shippingAddress.pincode
                    </p>

                    <p style="border-bottom: 1px solid #ddd;">Mobile:$consignment.mobilePhone</p>

                    <p style="border-bottom: 1px solid #ddd;">Office: $consignment.officePhone</p>
                </div>
                <div class="col-xs-6">
                    <p class="text-center">Phone:</p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <p>Through:-<strong><span style="font-size:15px">$consignment.through</span></strong></p>

                    <div class="row" style="line-height: 15px;">
                        <div class="col-xs-3">
                            <p>Payment Mode: <strong><span style="font-size:15px">$paid</span></strong></p>
                        </div>
                        <div class="col-xs-3">
                            <p>Delivery Mode: <strong><span style="font-size:15px">$deliveryMode</span></strong></p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6" style="padding-right: 20px;">
                        <p class="text-right" style="margin-right: 20px">for <strong>DACHEPALLI PUBLISHERS(P) LTD</strong></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        window.focus();
        window.print();
    </script>
</body>
</html>

