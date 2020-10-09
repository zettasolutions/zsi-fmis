    var safetyproblems = (function(){
    var  _public            = {}
        ,bs                 = zsi.bs.ctrl
        ,svn                = zsi.setValIfNull 
    ;
    zsi.ready = function(){
        $(".page-title").html("Safety Problems");
        $(".panel-container").css("min-height", $(window).height() - 160);
        displaySelects();
    };
    
    $("#btnSaveSafetyProblem").click(function () {
        $("#formSafetyProblem").jsonSubmit({
             procedure: "safety_problems_upd"
            ,isSingleEntry: true
            ,onComplete: function (data) {
                if(data.isSuccess){
                   if(data.isSuccess===true) zsi.form.showAlert("alert");
                   $("form").removeClass('was-validated');
                   $("#formSafetyProblem").find("input").val("");
                   $("#formSafetyProblem").find("textarea").val("");
                   $("#formSafetyProblem").find("select").val(null).trigger('change');
                   $("#myModal").find("#msg").text("Data successfully saved.");
                   $("#myModal").find("#msg").css("color","green");
                   setTimeout(function(){
                       $("#myModal").modal('toggle');
                       $("#safety_report_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
                       modalTxt();
                   },1000);
                }else{
                   $("#myModal").find("#msg").text("Something went wrong when saving the data.");
                   $("#myModal").find("#msg").css("color","red");
                }
            }
        }); 
    });
    
    function displaySelects(){
        $('#reported_by').select2({placeholder: "",allowClear: true});
        $('#safety_id').select2({placeholder: "",allowClear: true});
        $('#vehicle_id').select2({placeholder: "",allowClear: true});
        $("#safety_report_date,#closed_date").datepicker({todayHighlight:true,endDate:new Date()}).datepicker("setDate","0");
        
        $("#pms_type_id").dataBind({
            sqlCode      : "D235"
           ,text         : "pms_desc"
           ,value        : "pms_type_id"
        });
        $("#vehicle_id").dataBind({
            sqlCode      : "D272"
           ,parameters   : {client_id:app.userInfo.company_id}
           ,text         : "vehicle_plate_no"
           ,value        : "vehicle_id"
        });
        
        $("#reported_by").dataBind({
            sqlCode      : "D270" 
           ,parameters   : {client_id:app.userInfo.company_id}
           ,text         : "emp_lfm_name"
           ,value        : "id"
        });
        
        $("#safety_id").dataBind({
             sqlCode    : "D246"
            ,text       : "safety_name"
            ,value      : "safety_id"
        });
    }
    
    function modalTxt(){
        setTimeout(function(){
           $("#myModal").find("#msg").text("Are you sure you want to save this data?");
           $("#myModal").find("#msg").css("color","#000");
        },1000);
    }

    $("#submit").click(function () {
        var _$frm = $("#formSafetyProblem");
        var _frm = _$frm[0];
        var _formData = new FormData(_frm);  
        if( ! _frm.checkValidity() ){
            _$frm.addClass('was-validated');
        }else{   
            _$frm.removeClass('was-validated');
            $('#myModal').modal('show');
        }
    });
   
    
    return _public;
    
    
    
})();                     