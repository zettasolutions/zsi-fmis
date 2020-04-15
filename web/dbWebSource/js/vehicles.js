 (function(){
        
    var  bs         = zsi.bs.ctrl
        ,svn        = zsi.setValIfNull
        ,gVehicleMakerId   = null
        ,gActiveTab = ""
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Vehicles");
        displayVehicleMaker()
        displayVehicles(); 
        gActiveTab = "asset-type";
        
        $("#vehicleMakerId").select2({placeholder: "VEHICLE MAKER",allowClear: true});
        $('a[data-toggle="tab"]').on('shown.bs.tab', function(e){
            var target = $(e.target).attr("href"); 
            switch(target){
                case "#nav-vehicle_maker":
                    gActiveTab = "asset-type";
                    $("#searchVal").val("");
                    $("#vehicleMakerDiv").addClass("hide");
                    $("#dummyDiv").removeClass("hide");
                    displayVehicles(gVehicleMakerId);
                    break;
                case "#nav-vehicles":
                    gActiveTab = "assets";
                    $("#searchVal").val("");
                    $("#vehicleMakerDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#assetId").val(gVehicleMakerId).trigger('change');
                    break;
              default:break;
            } 
        }); 
        
        console.log("client",app.userInfo);
    }; 
    
    function displaySelects(){
        $("#vehicleMakerId").dataBind({
             sqlCode    : "D234" //dd_vehicle_maker_sel
            ,text       : "maker_name"
            ,value      : "vehicle_maker_id" 
            ,required   : true
            ,onChange   : function(){ 
                gVehicleMakerId = this.val();  
            }
        });
    }
    
    function displayVehicleMaker(searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridVehicleMaker").dataBind({
             sqlCode        : "V230" //vehicle_maker_sel
            ,parameters     : {search_val:(searchVal ? searchVal : "")}
            ,height         : $(window).height() - 273 
            ,blankRowsLimit : 5
            ,dataRows   : [
                     {text: cb                                                                          ,width:25           ,style:"text-align:left"
                         ,onRender : function(d){
                             return app.bs({name:"vehicle_maker_id"             ,type:"hidden"              ,value: app.svn(d,"vehicle_maker_id")}) 
                                  + app.bs({name:"is_edited"                    ,type:"hidden"              ,value: app.svn(d,"is_edited")})
                                  + (d !== null ? app.bs({name:"cb"             ,type:"checkbox"}) : "" );
                         }
                     }
                    ,{text:"Maker Code"             ,type:"input"       ,name:"maker_code"              ,width:100          ,style:"text-align:left"}
                    ,{text:"Maker Name"             ,type:"input"       ,name:"maker_name"              ,width:400          ,style:"text-align:left"}
                    
                  ]
                  ,onComplete : function(o){
                    var _dRows = o.data.rows;
                    var _this  = this;
        	        var _zRow  = _this.find(".zRow");
        	        if(_dRows.length < 1) $("#nav-tab").find("[aria-controls='nav-vehicles']").hide();
        	        _zRow.unbind().click(function(){
        	            var _self=this;
        	            setTimeout(function(){ 
            	            var _i      = $(_self).index();
            	            var _data   = _dRows[_i];
            	            var _vehicleMakerId  = _data.vehicle_maker_id;
            	            gVehicleMakerId = _vehicleMakerId;
            	            displaySelects();
            	            $("#nav-tab").find("[aria-controls='nav-vehicles']").show();
            	            setTimeout(function(){
            	                $("#vehicleMakerId").val(_vehicleMakerId).trigger('change');
            	            }, 200);
                            displayVehicles(_vehicleMakerId);

        	            }, 200);
        	        });
        	        _this.on('dragstart', function () {
                        return false;
                    });
                    this.find("[name='cbFilter1']").setCheckEvent("#gridVehicleMaker input[name='cb']");
                  } 
            });
        } 
    
    function displayVehicles(vehicle_maker_id,searchVal){  
        $("#gridVehicle").dataBind({
             sqlCode            : "V232" //vehicles_sel
            ,parameters         : {vehicle_maker_id: vehicle_maker_id,search_val:(searchVal ? searchVal : "")}
	        ,height             : $(window).height() - 273 
            ,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:"Plate No"                                                                                    ,width:100       ,style:"text-align:left"
                        ,onRender  :  function(d){ 
                                   return app.bs({name:"vehicle_id"             ,type:"hidden"      ,value: app.svn(d,"vehicle_id")}) 
                                        + app.bs({name:"is_edited"              ,type:"hidden"      ,value: app.svn(d,"is_edited")}) 
                                        + app.bs({name:"plate_no"               ,type:"input"       ,value: app.svn(d,"plate_no")});
                                        
                        }
                    } 
                    ,{text:"Conduction No"                       ,type:"input"          ,name:"conduction_no"           ,width:100       ,style:"text-align:left"}
                    ,{text:"Chassis No"                          ,type:"input"          ,name:"chassis_no"              ,width:100       ,style:"text-align:left"}
                    ,{text:"Engine No"                           ,type:"input"          ,name:"engine_no"               ,width:100       ,style:"text-align:left"}
                    ,{text:"Date Acquired"                                                                              ,width:120       ,style:"text-align:left"
                        ,onRender: function(d){ return app.bs({name:"date_acquired"     ,type:"input"    ,value: app.svn(d,"date_acquired").toShortDate()});
                
                        }
                    }
                    ,{text:"Exp Registration Date"                                                                      ,width:120       ,style:"text-align:left"
                        ,onRender: function(d){ return app.bs({name:"exp_registration_date"     ,type:"input"    ,value: app.svn(d,"exp_registration_date").toShortDate()});
                           
                        }
                    }
                    ,{text:"Exp Insurance Date"                                                                         ,width:120       ,style:"text-align:left"
                        ,onRender: function(d){ 
                            return app.bs({name:"exp_insurance_date"        ,type:"input"       ,value: app.svn(d,"exp_insurance_date").toShortDate()})
                                 + app.bs({name:"vehicle_maker_id"          ,type:"hidden"      ,value: vehicle_maker_id});
                        }
                    }
                    ,{text:"Odometer Reading"                   ,type:"input"           ,name:"odometer_reading"         ,width:150       ,style:"text-align:left"}
                    ,{text:"Active?"                            ,type:"yesno"           ,name:"is_active"                ,width:60        ,style:"text-align:left"     ,defaultValue:"Y"}
                    ,{text:"Status"                                                                                      ,width:100       ,style:"text-align:left"
                        ,onRender  :  function(d){ 
                                   return app.bs({name:"status_id"              ,type:"select"       ,value: app.svn(d,"status_id")}) 
                                        + app.bs({name:"hash_key"               ,type:"hidden"       ,value: app.svn(d,"hash_key")})
                                        + app.bs({name:"client_id"              ,type:"hidden"       ,value: app.userInfo.client_id});
                                        
                        }
                    }
                ] 
                ,onComplete : function(d){
                    this.find("#asset_type_id").attr("selectedvalue",d.asset_type_id);
                    this.find("[name='date_acquired']").datepicker({todayHighlight:true}); 
                    this.find("[name='exp_registration_date']").datepicker({todayHighlight:true}); 
                    this.find("[name='exp_insurance_date']").datepicker({todayHighlight:true}); 
                    this.find("select[name='status_id']").dataBind({
                         sqlCode    : "S122" //statuses_sel
                        ,text       : "status_code"
                        ,value      : "status_desc"
                    }); 
                    
                } 
            });
        }
        
    function displayInactiveVehicles(vehicle_maker_id){
         var cb = app.bs({name:"cbFilter2",type:"checkbox"});
         $("#gridInactiveVehicles").dataBind({
    	     sqlCode            : "V232" //vehicles_sel
            ,parameters         : {vehicle_maker_id: vehicle_maker_id,is_active: "N"}
	        ,height             : 360 
            ,dataRows           : [
                    {text:cb        ,width:25              ,style : "text-align:left"
                        ,onRender  :  function(d){ 
                                    return app.bs({name:"vehicle_id"               ,type:"hidden"      ,value: app.svn(d,"vehicle_id")}) 
                                         + app.bs({name:"is_edited"              ,type:"hidden"      ,value: app.svn(d,"is_edited")})
                                         + (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    } 
                    ,{text:"Plate No"                                                           ,width:240       ,style:"text-align:left"
                        ,onRender: function(d){ 
                            return app.bs({name:"plate_no"                  ,type:"input"       ,value: app.svn(d,"plate_no")})
                                 + app.bs({name:"conduction_no"             ,type:"hidden"      ,value: app.svn(d,"conduction_no")})
                                 + app.bs({name:"chassis_no"                ,type:"hidden"      ,value: app.svn(d,"chassis_no")})
                                 + app.bs({name:"engine_no"                 ,type:"hidden"      ,value: app.svn(d,"engine_no")})
                                 + app.bs({name:"date_acquired"             ,type:"hidden"      ,value: app.svn(d,"date_acquired").toShortDate()})
                                 + app.bs({name:"exp_registration_date"     ,type:"hidden"      ,value: app.svn(d,"exp_registration_date").toShortDate()})
                                 + app.bs({name:"exp_insurance_date"        ,type:"hidden"      ,value: app.svn(d,"exp_insurance_date").toShortDate()})
                                 + app.bs({name:"vehicle_maker_id"          ,type:"hidden"      ,value: app.svn(d,"vehicle_maker_id")})
                                 + app.bs({name:"odometer_reading"          ,type:"hidden"      ,value: app.svn(d,"odometer_reading")});
                
                        }
                    }
                    ,{text:"Active?"                                                            ,width:60        ,style:"text-align:left"     ,defaultValue:"N"
                        ,onRender: function(d){ 
                            return app.bs({name:"is_active"                 ,type:"yesno"       ,value: app.svn(d,"is_active")})
                                 + app.bs({name:"status_id"                 ,type:"hidden"      ,value: app.svn(d,"status_id")})
                                 + app.bs({name:"hash_key"                  ,type:"hidden"      ,value: app.svn(d,"hash_key")})
                                 + app.bs({name:"client_id"                 ,type:"hidden"      ,value: app.userInfo.client_id});
                
                        }
                    }
                ] 
                ,onComplete : function(d){    
                    this.find("[name='cbFilter2']").setCheckEvent("#gridInactiveVehicles input[name='cb']");  
                }
        });    
    }
    
    $("#btnInactive").click(function(){
        $(".modal-title").text("Inactive Vehicle(s)");
        $('#modalInactive').modal({ show: true, keyboard: false, backdrop: 'static' });
        displayInactiveVehicles(gVehicleMakerId);
        
    });
    
    $("#btnSaveInactive").click(function(){
       $("#gridInactiveVehicles").jsonSubmit({
                 procedure: "vehicles_upd"
                ,optionalItems: ["is_active","status_id"]
                ,onComplete: function (data) {
                    if(data.isSuccess===true) zsi.form.showAlert("alert");
                    displayInactiveVehicles(gVehicleMakerId);
                    displayVehicles(gVehicleMakerId);
                    $('#modalInactive').modal('toggle');
                }
        });
    });
        
    $("#btnSaveVehicleMaker").click(function(){
        $("#gridVehicleMaker").jsonSubmit({
            procedure:"vehicle_maker_upd"
            ,onComplete:function(data){
                if(data.isSuccess===true)zsi.form.showAlert("alert");
                displayVehicleMaker();
            }
        });
    });
    
    $("#btnDeleteVehicleMaker").click(function(){
        zsi.form.deleteData({ 
                code:"ref-00011"
               ,onComplete:function(data){
                     displayVehicleMaker();
               }
        });
    });
            
    $("#btnSaveVehicle").click(function(){ 
        $("#gridVehicle").jsonSubmit({
             procedure: "vehicles_upd"
            ,optionalItems: ["is_active","status_id"] 
            ,onComplete: function (data) { 
               if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                displayVehicles(gVehicleMakerId);
            } 
        }); 
    });
    
    $("#btnDeleteVehicles").click(function(){ 
        zsi.form.deleteData({ 
            code:"ref-00012"
           ,onComplete:function(data){
                displayInactiveVehicles(gVehicleMakerId);
                displayVehicles(gVehicleMakerId);
                $('#modalInactive').modal('toggle');
           }
        });
    });
    
    $("#btnFilterAsset").click(function(){ 
        displayVehicles(gVehicleMakerId);
    });
    
    $("#btnSearchVal").click(function(){ 
        var _searchVal = $.trim($("#searchVal").val()); 
        if(gActiveTab === "nav-vehicles") displayVehicleMaker(_searchVal);
        else displayVehicles(gVehicleMakerId,_searchVal);
        
    }); 
   $("#searchVal").on('keypress',function(e){
        var _searchVal = $.trim($("#searchVal").val()); 
        if(e.which == 13) {
           if(gActiveTab === "nav-vehicles") displayVehicleMaker(_searchVal);
           else displayVehicles(gVehicleMakerId,_searchVal);
        }
    });

    $("#searchVal").keyup(function(){
        if($(this).val() === "") {
            if(gActiveTab === "nav-vehicles") displayVehicleMaker();
            else displayVehicles(gVehicleMakerId);
        }
    });
    
    $("#btnResetVal").click(function(){
        $("#searchVal").val("");
        $("#nav-tab").find("[aria-controls='nav-vehicles']").hide();
    });
    
})();     