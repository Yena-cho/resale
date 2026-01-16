<script>
    $(document).ready(function () {
        menuLocation();

        var xhr = new XMLHttpRequest();

        $(document).ajaxStart(function () {
            xhr.startTime = new Date().getTime();
            $(".spinner-area").show();//checks if the ajax has finished yet before displaying #loader
        }).ajaxStop(function () {
            var elapsed = new Date().getTime() - xhr.startTime;
            if (elapsed < 1000) {
                $(".spinner-area").delay(300).hide(0);
            } else {
                $(".spinner-area").hide();
            }
        });
    });

    function menuLocation() {
        $("#" + oneDepth).addClass("active");
        $("#" + twoDepth).addClass("active");
    }
</script>
        <div class="spinner-area" style="display:none;">
            <div class="sk-spinner sk-spinner-cube-grid">
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
                <div class="sk-cube"></div>
            </div>
            <div class="modal-backdrop fade in"></div>
        </div>
    </div>
</div>

<script src="/assets/bootstrap/bootstrap-for-admin.min.js"></script>
<script src="/assets/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/assets/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<script src="/assets/js/inspinia.js"></script>
<script src="/assets/js/plugins/pace/pace.min.js"></script>
<script src="/assets/js/admin-sweetalert.js"></script>

<script src="/assets/js/moment.js"></script>
</body>

</html>
