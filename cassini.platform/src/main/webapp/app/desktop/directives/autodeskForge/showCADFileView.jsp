<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 17-05-2019
  Time: 13:46
  To change this template use File | Settings | File Templates.
--%>
<div>
<style scoped>

    .forge-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        /*  z-index: 1; /!* Sit on top *!/*/
        /*padding-top: 100px; /!* Location of the box *!/*/
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .forge-model .closeImage1 {
        position: absolute;
        top: 10px !important;
        right: 10px !important;;
        color: blanchedalmond;
        font-size: 30px;
        font-weight: bold;
        transition: 0.3s;
        border: 1px solid lightgrey;
        border-radius: 50%;
        background: #988282a6;
        padding: 5px;
    }

    .forge-model .closeImage1:hover,
    .forge-model .closeImage1:focus {
        color: white;
        text-decoration: none;
        cursor: pointer;
    }

    #forgeFrame {
        width: 100%;
        height: 96%;
        margin: 0;
        background-color: #F0F8FF;
    }
</style>
<div id="forgeModel" class="forge-model modal">
    <span class="closeImage1">&times;</span>
    <iframe id="forgeFrame"
            src=""
            frameborder="0" height="100%" width="100%"></iframe>
</div>
</div>