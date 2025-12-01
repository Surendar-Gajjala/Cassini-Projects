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
    <div class="row">
        <div class="col-xs-12 text-center  text-uppercase text-center">
            <h3 class="">navata road transport,
                <small>vijayawada</small>
            </h3>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12  text-uppercase text-center">goods forwarding note</div>
    </div>
    <div class="row">
        <div class="col-xs-8 col-xs-offset-2">
            <div class="row">
                <div class="col-xs-6"><strong>Station:</strong></div>
                <div class="col-xs-6 text-right">Date: $date</div>
            </div>
            <div class="row">
                <div class="col-xs-12">Sir, Please accept the following consignment(s) for booking through your lorry
                    from
                    <strong>as per details given below</strong>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <table class="table table-bordered mrtop20">

                <tr>
                    <td class="text-center pdall">Name and full Postal address of the consignor</td>
                    <td class="text-center"><strong>DACHEPALLI PUBLISHERS PVT LTD</strong> <br/>
                        Plot No.2/B,(C.F.Area)
                        I.D.A. Cherlapalli,
                        Phase 2,Hyderabad-51
                        ph:040-64579788,65220941
                    </td>
                    <td class="text-center pdall">Mode of payment<br>
                        $paid
                    </td>
                </tr>
                <tr>
                    <td class="text-center pdall">Name and full Postal address of the consignee<br>
                        (if address to SeLf the word SELF may be written)
                    </td>
                    <td class="text-center pdall"><strong>$consignment.consignee</strong><br>
                        $consignment.shippingAddress.addressText <br>
                        $consignment.shippingAddress.city,$consignment.shippingAddress.state.name -
                        $consignment.shippingAddress.pincode <br>
                        Mobile Phone: $consignment.mobilePhone <br>
                        Office Phone: $consignment.officePhone <br>
                    </td>
                    <td class="text-center pdall">Which basis<br><strong>WITH PASS</strong></td>
                </tr>
                <tr>
                    <td class="text-center pdall">Number and description of article or articles</td>
                    <td class="pdall"><span class="text-left">$consignment.contents</span><br>
                        <span class="text-right">Value in Rs: &#8377;$value</span>
                    </td>
                    <td class="text-center  pdall">Service Tax Payable by</td>
                </tr>
            </table>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div class="checkbox text-center" style="margin: 0px;">
                <label style="padding-left: 0px;">
                    <i class="fa fa-check"></i>
                    I do hereby certify that i have satisfied my self with full knowledge recorded
                    on the reverse and which conditions i accept
                </label>
            </div>
        </div>
    </div>
    <div class="row" style="margin-bottom: -6px;">
        <div class="col-xs-6"><strong>FOR OFFICE ONLY</strong></div>
        <div class="col-xs-6 text-right" style="margin-bottom: 20px; margin-top: 20px;"><strong>Signature of the
            Consignor</strong>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div style="margin-top: -33px;" class="row">
                <div class="col-xs-3">
                    <div style="font-size: 15px;"><strong>Waybill no: $invoiceNumber</strong></div>
                    <div>Actual weight</div>
                    <div>DCC</div>
                    <div><strong>Marked Staff No</strong></div>
                </div>
                <div class="col-xs-3" style="padding-left: 68px;"><p style="margin-bottom: 1px;">CDM</p>

                    <div>DBC</div>
                    <div style="margin-top: 10px;"><strong>Operator staff No</strong></div>
                </div>
                <div class="col-xs-6 class=" pull-right
                "" style="padding-left: 71px;"><p style="margin-bottom: 1px;">Destination Code</p>

                <div>Miscellaneous Charges</div>
                <div><strong>Checked Agent/staff No.</strong></div>
            </div>
        </div>
    </div>
    <div class="row" style="line-height: 12px; margin-left: 3px;">
        <div class="col-md-4">
            <p style="margin-left: 10px !important;">Payment Mode: <strong><span style="font-size:15px">$paid</span></strong></p>
        </div>
        <div class="col-md-4">
            <p>Delivery Mode: <strong><span style="font-size:15px">$deliveryMode</span></strong></p>
        </div>
        <div class="col-md-4">
            <p>Through:-<strong><span style="font-size:15px">$consignment.through</span></strong></p>
        </div>
    </div>
    <div class="col-sm-3"></div>
</div>
<br><br>

<%
    for(int i=0; i<2; i++) {
%>
<br><br><br>
<div class="page-break"></div>
<hr class="break-line" style="margin-bottom: 30px;"/>
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
                    Dated: $date Of <strong style="font-size: 15px;">$consignment.contents PRINTED BOOKS GUNNY BUNDLES</strong>
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
<%
    }
%>
<script>
    window.focus();
    window.print();
</script>
</body>
</html>