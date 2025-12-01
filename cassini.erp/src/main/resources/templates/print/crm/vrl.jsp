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
<body>
<div class="content contentPanel">
    <div class="row">
        <div class="col-xs-8" style="text-align: center;">
            <h5>SUBJECT TO HUBBALI JURISDICTION</h5>
            <h4><strong>VRL LOGISTICS LTD</strong></h4>
        </div>
        <div class="col-xs-4">

            <table class="table table-bordered">
                <tbody>
                <tr>
                    <td><strong>CUSTOMER CARE</strong></td>
                </tr>

                <tr>
                    <td><strong>VARUR-H0 HUBBALLI</strong></td>
                </tr>

                <tr>
                    <td><strong><span class="mr5">Phone:</span>0836-2307800</strong></td>
                </tr>
                </tbody>
            </table>
        </div>

    </div>

    <%
        def date = new Date().format("dd-MM-yyyy");
        def pattern = "##,###.##";
        def moneyform = new java.text.DecimalFormat(pattern);
        String value = moneyform.format(consignment.value);

        String paid = "TO-PAY";
        if(consignment.paidBy == com.cassinisys.erp.model.crm.PaidBy.SELF) {
            paid = "PAID";
        }

        String deliveryMode = "WITH PASS";
        if(consignment.deliveryMode == com.cassinisys.erp.model.crm.DeliveryMode.DOOR_DELIVERY) {
            deliveryMode = "DOOR DELIVERY";
        }
        else if(consignment.deliveryMode == com.cassinisys.erp.model.crm.DeliveryMode.ORDINARY) {
            deliveryMode = "ORDINARY";
        }

        String invoiceNumber = "";
        if(consignment.shipments != null && consignment.shipments.size() > 0) {
            invoiceNumber = consignment.shipments.iterator().next().invoiceNumber;
        }
    %>

    <div class="row">
        <div class="col-md-12">
            <h5>CORP OFF:Giriraj Annexe Circuit House Road,HUBBALLI-580 029(Karnataka)ph:0836-2237511</h5>
        </div>


    </div>

    <div class="row">
        <div class="col-md-12" style="line-height: 13px;">
            <h5><strong>OWNER'S RISK FORWARDING NOTE</strong></h5>

            <P><strong>To.VRL Logistics Ltd</strong>................</P>

            <p>Please receive the under-mentioned goods & forward by Lorry From........................</p>

            <p>to.......................consigned as below.</p>

            <div>
                <h5 class="text-right"><strong>Date:$date</strong></h5>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Consignor name & address</th>
                    <th>Consignee's name & address
                        (If documents through Banks)
                        Banker's name should be written
                    </th>
                    <th>No.of Articles</th>
                    <th>Name & Description of the goods</th>
                    <th style="width: 50px">Weight <br/>(kgs)</th>
                    <th>Value</th>
                    <th>Freight</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="text-center"><strong>DACHEPALLI PUBLISHERS PVT LTD</strong> <br/>
                        Plot No.2/B,(C.F.Area)
                        I.D.A. Cherlapalli,
                        Phase 2,Hyderabad-51
                        ph:040-64579788,65220941
                    </td>
                    <td class="text-center"><strong>$consignment.consignee</strong> <br/>
                        $consignment.shippingAddress.addressText<br/>
                        $consignment.shippingAddress.city, $consignment.shippingAddress.state.name - $consignment.shippingAddress.pincode<br/>
                        Mobile Phone: $consignment.mobilePhone <br/>
                        Office Phone: $consignment.officePhone
                    </td>
                    <td class="text-center">$consignment.contents</td>
                    <td class="text-center">$consignment.description</td>
                    <td style="width:50px;"></td>
                    <td class="text-center">&#8377;$value</td>
                    <td class="text-center">$paid</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12" style="margin-bottom: -18px;">
            <p style="line-height: 12px;"> I/We do hereby certify that,I/We have satisfied myself/ourselves that the description,marks,value,weight&quantity of
                goods consigned by me/us have been truly entered above in this forwarding note,The contents of package free from a id
                and its allied products or Explosive,Inflammable or contraband goods prohibited by law.That the consignment is booked
                with best of my/our knowledge,Information and belief.Consignment booked by making false declaration of goods are liable
                for forfeiture forthwith on detection and liable for prosecution.
            </p>
        </div>
    </div>
    <br>

    <div class="row" style="line-height: 15px;">
        <div class="col-sm-2">
            <p><strong>Payment Mode:</strong><span style="font-size:15px"><strong>$paid</strong></span></p>
        </div>
        <div class="col-sm-2">
            <p><strong>Delivery Mode:</strong><span style="font-size:15px"><strong>$deliveryMode</strong></span></p>
        </div>
        <div class="col-sm-2">
            <p style="font-size: 15px;"><strong>Way Bill No: $invoiceNumber </strong></p>
        </div>
        <div class="col-sm-6">
            <p><strong>Through:-</strong><span style="font-size:15px"><strong>$consignment.through</strong></span></p>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div align="right">
                <p><strong>Signature of consignor</strong></p>
            </div>
        </div>
    </div>
    <br><br>

    <div class="page-break"></div>

    <hr class="break-line" style="margin-bottom: 30px;"/>

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
    <div class="row" style="line-height: 13px;">
        <div class="col-md-12">
            <h5 class="text-center"><strong style="border-bottom: 1px solid #ddd;">DECLARATION</strong></h5>

            <P>WE DO HERE BY DECLARE that the consignment booked under PAID or TO-PAY parcel</P>

            <div class="row">
                <div class="col-sm-2">
                   <strong><p style="font-size: 14px;">Way Bill No: $invoiceNumber </p></strong>
                </div>

                <div class="col-sm-10">
                    Dated: $date Of <strong style="font-size: 15px;">$consignment.contents PRINTED BOOKS GUNNY BUNDLES</strong>
                </div>
            </div>
            <P>Approximately valued of <strong>&#8377;$value</strong> are printed books which are exempted from payment of sales Tax.</P>
        </div>
    </div>
    <div class="row" style="line-height: 13px;">
        <div class="col-xs-6" style="margin-top: -1px;">
            <p><strong style="line-height: 29px;">Name and address of the consignee</strong></p>

          <p style="border-bottom: 1px solid #ddd; font-size: 18px;"><strong>$consignment.consignee</strong></p>

            <p style="border-bottom: 1px solid #ddd;">$consignment.shippingAddress.addressText</p>

            <p style="border-bottom: 1px solid #ddd;">
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
            <p><strong>Through:-</strong><span style="font-size:15px"><strong>$consignment.through</strong></span></p>

            <div class="row" style="line-height: 13px;">
                <div class="col-xs-3">
                    <p><strong>Payment Mode:</strong><span style="font-size:15px"><strong>$paid</strong></span></p>
                </div>
                <div class="col-xs-3">
                    <p><strong>Delivery Mode:</strong><span style="font-size:15px"><strong>$deliveryMode</strong></span></p>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <p class="text-right" style="margin-right: 20px">for <strong>DACHEPALLI PUBLISHERS(P) LTD</strong></p>
            </div>
        </div>
    </div>
    <br><br>

    <div class="page-break"></div>

    <hr class="break-line" style="margin-bottom: 30px;"/>

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
    <div class="row" style="line-height: 13px;">
        <div class="col-md-12">
            <h5 class="text-center"><strong style="border-bottom: 1px solid #ddd;">DECLARATION</strong></h5>

            <P>WE DO HERE BY DECLARE that the consignment booked under PAID or TO-PAY parcel</P>

            <div class="row">
                <div class="col-sm-2">
                    <strong><p style="font-size: 14px;">Way Bill No: $invoiceNumber </p></strong>
                </div>

                <div class="col-sm-10">
                    Dated: $date Of <strong style="font-size: 15px;">$consignment.contents PRINTED BOOKS GUNNY BUNDLES</strong>
                </div>
            </div>
            <P>Approximately valued of <strong>&#8377;$value</strong> are printed books which are exempted from payment of sales Tax.</P>
        </div>
    </div>
    <div class="row" style="line-height: 13px;">
        <div class="col-xs-6" style="margin-top: -1px;">
            <p><strong style="line-height: 29px;">Name and address of the consignee</strong></p>
          <p style="border-bottom: 1px solid #ddd;font-size: 18px;"><strong>$consignment.consignee</strong></p>
            <p style="border-bottom: 1px solid #ddd;">$consignment.shippingAddress.addressText</p>

            <p style="border-bottom: 1px solid #ddd;">
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
            <p><strong>Through:-</strong><span style="font-size:15px"><strong>$consignment.through</strong></span></p>

            <div class="row" style="line-height: 13px;">
                <div class="col-xs-3">
                    <p><strong>Payment Mode:</strong><span style="font-size:15px"><strong>$paid</strong></span></p>
                </div>
                <div class="col-xs-3">
                    <p><strong>Delivery Mode:</strong><span style="font-size:15px"><strong>$deliveryMode</strong></span></p>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <p class="text-right" style="margin-right: 20px">for <strong>DACHEPALLI PUBLISHERS(P) LTD</strong></p>
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

