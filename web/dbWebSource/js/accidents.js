    var accidents = (function(){
    var  _public            = {}
        ,bs                 = zsi.bs.ctrl
        ,svn                = zsi.setValIfNull 
    ;
    zsi.ready = function(){
        $(".page-title").html("Accident");
        $(".panel-container").css("min-height", $(window).height() - 160);
        displaySelects();
    };
    
    $("#btnSaveAccident").click(function () {
        $("#formAccident").jsonSubmit({
             procedure: "accident_upd"
            ,isSingleEntry: true
            ,onComplete: function (data) {
                if(data.isSuccess){
                   if(data.isSuccess===true) zsi.form.showAlert("alert");
                   $("form").removeClass('was-validated');
                   $("#formAccident").find("input").val("");
                   $("#formAccident").find("textarea").val("");
                   $("#formAccident").find("select").val(null).trigger('change');
                   $("#myModal").find("#msg").text("Data successfully saved.");
                   $("#myModal").find("#msg").css("color","green");
                   setTimeout(function(){
                       $("#myModal").modal('toggle');
                       $("#accident_date").datepicker({todayHighlight:true}).datepicker("setDate",new Date());
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
        $('#pao_id').select2({placeholder: "",allowClear: true});
        $('#driver_id').select2({placeholder: "",allowClear: true});
        $('#vehicle_id').select2({placeholder: "",allowClear: true});
        $('#gas_station').select2({placeholder: "",allowClear: true});
        $('#error_type_id').select2({placeholder: "",allowClear: true});
        $('#accident_type_id').select2({placeholder: "",allowClear: true});
        $("#accident_date").datepicker({todayHighlight:true,endDate:new Date()}).datepicker("setDate","0");
        
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
        
        $("#driver_id").dataBind({
            sqlCode      : "D270" 
           ,parameters   : {client_id:app.userInfo.company_id}
           ,text         : "emp_lfm_name"
           ,value        : "id"
        });
        
        $("#pao_id").dataBind({
            sqlCode      : "D271" 
           ,parameters   : {client_id:app.userInfo.company_id}
           ,text         : "emp_lfm_name"
           ,value        : "id"
        });
        
        $("#gas_station").dataBind({
             sqlCode    : "G215"
            ,text       : "gas_station_name"
            ,value      : "gas_station_id"
        });
        
        $("#accident_type_id").dataBind({
             sqlCode    : "D243"
            ,text       : "accident_type"
            ,value      : "accident_type_id"
        });
        
        $("#error_type_id").dataBind({
             sqlCode    : "D244" 
            ,text       : "error_type"
            ,value      : "error_type_id"
        });
    }
    
    function modalTxt(){
        setTimeout(function(){
           $("#myModal").find("#msg").text("Are you sure you want to save this data?");
           $("#myModal").find("#msg").css("color","#000");
        },1000);
    }

    $("#submit").click(function () {
        var _$frm = $("#formAccident");
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