(function(){
        
    var  bs         = zsi.bs.ctrl
        ,svn        = zsi.setValIfNull
        ,gAssetId   = null
        ,gActiveTab = ""
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Assets");
        displayAssetType()
        displayAssets(); 
        gActiveTab = "asset-type";
        
        $("#assetId").select2();
        $('a[data-toggle="tab"]').on('shown.bs.tab', function(e){
            var target = $(e.target).attr("href"); 
            switch(target){
                case "#nav-asset-type":
                    gActiveTab = "asset-type";
                    $("#searchVal").val("");
                    $("#assetDiv").addClass("hide");
                    $("#dummyDiv").removeClass("hide");
                    displayAssets(gAssetId);
                    break;
                case "#nav-assets":
                    gActiveTab = "assets";
                    $("#searchVal").val("");
                    $("#assetDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#assetId").val(gAssetId).trigger('change');
                    break;
              default:break;
            } 
        }); 
    }; 
    
    function displaySelects(){
        $("#assetId").dataBind({
             sqlCode    : "A163" 
            ,text       : "asset_type"
            ,value      : "id" 
            ,required   : true
            ,onChange   : function(){ 
                gAssetId = this.val();  
            }
        });
    }
    
    function displayAssetType(searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridAssetType").dataBind({
             sqlCode        : "A163" //asset_types_sel
            ,parameters     : {search_val:(searchVal ? searchVal : "")}
            ,height         : $(window).height() - 273 
            ,blankRowsLimit : 5
            ,dataRows   : [
                     {text: cb                                  ,width:25                   ,style:"text-align:left"
                         ,onRender : function(d){
                             return app.bs({name:"id"               ,type:"hidden"              ,value: app.svn(d,"id")}) 
                                +   bs({name:"is_edited"            ,type:"hidden"              ,value: app.svn(d,"is_edited")})
                                + (d !== null ? app.bs({name:"cb"   ,type:"checkbox"}) : "" );
                         }
                     }
                    ,{text:"Asset Code"             ,type:"input"       ,name:"asset_code"              ,width:100          ,style:"text-align:left"}
                    ,{text:"Asset Type"             ,type:"input"       ,name:"asset_type"              ,width:400          ,style:"text-align:left"}
                    
                  ]
                  ,onComplete : function(o){
                    var _dRows = o.data.rows;
                    var _this  = this;
        	        var _zRow  = _this.find(".zRow");
        	        if(_dRows.length < 1) $("#nav-tab").find("[aria-controls='nav-assets']").hide();
        	        _zRow.unbind().click(function(){
        	            var _self=this;
        	            setTimeout(function(){ 
            	            var _i      = $(_self).index();
            	            var _data   = _dRows[_i];
            	            var _assetId  = _data.id;
            	            gAssetId = _assetId;
            	            displaySelects();
            	            $("#nav-tab").find("[aria-controls='nav-assets']").show();
            	            setTimeout(function(){
            	                $("#assetId").val(_assetId).trigger('change');
            	            }, 200);
                            displayAssets(_assetId);

        	            }, 200);
        	        });
        	        _this.on('dragstart', function () {
                        return false;
                    });
                    $("#cbFilter1").setCheckEvent("#gridAssetType input[name='cb']");
                  } 
            });
        } 
    
    function displayAssets(asset_id,searchVal){  
        $("#gridAssets").dataBind({
             sqlCode            : "A165" //assets_sel
            ,parameters         : {asset_id: asset_id,search_val:(searchVal ? searchVal : "")}
	        ,height             : $(window).height() - 273 
            ,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:"Asset No"                                                                ,width:100       ,style:"text-align:left"
                        ,onRender  :  function(d){ 
                                   return app.bs({name:"asset_id"               ,type:"hidden"      ,value: app.svn(d,"asset_id")}) 
                                        + app.bs({name:"is_edited"              ,type:"hidden"      ,value: app.svn(d,"is_edited")}) 
                                        + app.bs({name:"asset_type_id"          ,type:"hidden"      ,value: asset_id})
                                        + app.bs({name:"asset_no"               ,type:"input"       ,value: app.svn(d,"asset_no")});
                                        
                        }
                    } 
                    ,{text:"Date Acquired"                ,width:100       ,style:"text-align:left"
                        ,onRender: function(d){ return app.bs({type:"input"    ,name:"date_acquired"    ,value: app.svn(d,"date_acquired").toShortDate()});
                
                        }
                    }  
                    ,{text:"Exp Registration Date"        ,width:120       ,style:"text-align:left"
                        ,onRender: function(d){ return app.bs({type:"input"    ,name:"exp_registration_date"    ,value: app.svn(d,"exp_registration_date").toShortDate()});
                
                        }
                    }
                    ,{text:"Exp Insurance Date"           ,width:120       ,style:"text-align:left"
                        ,onRender: function(d){ return app.bs({type:"input"    ,name:"exp_insurance_date"    ,value: app.svn(d,"exp_insurance_date").toShortDate()});
                
                        }
                    }
                    ,{text:"Status"                       ,type:"select"          ,name:"status_id"                ,width:150       ,style:"text-align:left"}
                    ,{text:"Active?"                      ,type:"yesno"           ,name:"is_active"                ,width:60        ,style:"text-align:left"     ,defaultValue:"Y"} 
                ] 
                ,onComplete : function(d){
                    this.find("#asset_type_id").attr("selectedvalue",d.asset_type_id);
                    this.find("[name='date_acquired']").datepicker({todayHighlight:true}); 
                    this.find("[name='exp_registration_date']").datepicker({todayHighlight:true}); 
                    this.find("[name='exp_insurance_date']").datepicker({todayHighlight:true}); 
                    this.find("select[name='status_id']").dataBind({
                         sqlCode    : "A181" 
                        ,text       : "asset_status"
                        ,value      : "asset_status_id" 
                    }); 
                    
                } 
            });
        }
        
    function displayInactiveAssets(asset_id){
         var cb = app.bs({name:"cbFilter2",type:"checkbox"});
         $("#gridInactiveAssets").dataBind({
    	     sqlCode            : "A165" //assets_sel
            ,parameters         : {asset_id: asset_id,is_active: "N"}
	        ,height             : 360 
            ,dataRows           : [
                    {text:cb        ,width:25              ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"asset_id"   ,type:"hidden"      ,value: app.svn(d,"asset_id")}) 
                                        + app.bs({name:"is_edited"                  ,type:"hidden"      ,value: app.svn(d,"is_edited")}) 
                                        + app.bs({name:"asset_type_id"              ,type:"hidden"      ,value: asset_id})
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    } 
                    ,{text:"Asset No"                                                           ,width:240       ,style:"text-align:left"
                        ,onRender: function(d){ 
                            return app.bs({name:"asset_no"                  ,type:"input"       ,value: app.svn(d,"asset_no")})
                                 + app.bs({name:"date_acquired"             ,type:"hidden"      ,value: app.svn(d,"date_acquired").toShortDate()})
                                 + app.bs({name:"exp_registration_date"     ,type:"hidden"      ,value: app.svn(d,"exp_registration_date").toShortDate()})
                                 + app.bs({name:"exp_insurance_date"        ,type:"hidden"      ,value: app.svn(d,"exp_insurance_date").toShortDate()})
                                 + app.bs({name:"status_id"                 ,type:"hidden"      ,value: app.svn(d,"status_id")});
                
                        }
                    }
                    ,{text:"Active?"                ,type:"yesno"           ,name:"is_active"   ,width:60        ,style:"text-align:left"     ,defaultValue:"N"} 
                ] 
                ,onComplete : function(d){    
                    this.find("#cbFilter2").setCheckEvent("#gridInactiveAssets input[name='cb']");  
                }
        });    
    }
    
    $("#btnInactive").click(function(){
        $(".modal-title").text("Inactive Asset(s)");
        $('#modalInactive').modal({ show: true, keyboard: false, backdrop: 'static' });
        displayInactiveAssets(gAssetId);
        
    });
    
    $("#btnSaveInactive").click(function(){
       $("#gridInactiveAssets").jsonSubmit({
                 procedure: "assets_upd"
                ,optionalItems: ["is_active"]
                ,onComplete: function (data) {
                    if(data.isSuccess===true) zsi.form.showAlert("alert");
                    displayInactiveAssets(gAssetId);
                    displayAssets(gAssetId);
                    $('#modalInactive').modal('toggle');
                }
        });
    });
        
    $("#btnSaveAssetType").click(function(){
        $("#gridAssetType").jsonSubmit({
            procedure:"asset_type_upd"
            ,onComplete:function(data){
                if(data.isSuccess===true)zsi.form.showAlert("alert");
                displayAssetType();
            }
        });
    });
    
    $("#btnDeleteAssetType").click(function(){
        zsi.form.deleteData({ 
                code:"ref-00010"
               ,onComplete:function(data){
                     displayAssetType();
               }
        });
    });
            
    $("#btnSaveAssets").click(function(){ 
        $("#gridAssets").jsonSubmit({
             procedure: "assets_upd"
            ,optionalItems: ["is_active"] 
            ,onComplete: function (data) { 
               if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                displayAssets(gAssetId);
            } 
        }); 
    });
    
    $("#btnDeleteAssets").click(function(){ 
        zsi.form.deleteData({ 
            code:"ref-00011"
           ,onComplete:function(data){
                displayInactiveAssets(gAssetId);
                displayAssets(gAssetId);
                $('#modalInactive').modal('toggle');
           }
        });
    });
    
    $("#btnFilterAsset").click(function(){ 
        displayAssets(gAssetId);
    });
    
    $("#btnSearchVal").click(function(){ 
        var _searchVal = $.trim($("#searchVal").val()); 
        if(gActiveTab === "asset-type") displayAssetType(_searchVal);
        else displayAssets(gAssetId,_searchVal);
        
    }); 
   $("#searchVal").on('keypress',function(e){
        var _searchVal = $.trim($("#searchVal").val()); 
        if(e.which == 13) {
           if(gActiveTab === "asset-type") displayAssetType(_searchVal);
           else displayAssets(gAssetId,_searchVal);
        }
    });

    $("#searchVal").keyup(function(){
        if($(this).val() === "") {
            if(gActiveTab === "asset-type") displayAssetType();
            else displayAssets(gAssetId);
        }
    });
    
    $("#btnResetVal").click(function(){
        $("#searchVal").val("");
        $("#nav-tab").find("[aria-controls='nav-assets']").hide();
    });
    
})(); 