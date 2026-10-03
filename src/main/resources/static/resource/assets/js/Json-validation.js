			var a1 = "", a2 = "", a3 = "", a4 = "", a5 = "", a6 = "", a7 = "", a8 = "", a9 = "", a10 = "", a12 = "", a13 = "", a15 = "", a16 = "", a18 = "", a19 = "", a20 = "", a21 = "", a22 = "",a23 = "", a24 = "",a25 = "",a26 = "", a27 = "",a28 = "",a29 = "",a30 = "",a31 = "",a32 = "",a33= "",a34 = "",a35= "",
            arr = [], arr1 = [], i = 0, j = 0, k = 0, m = 0 , x = 0,
            text = false,text1 = false,numeric = false,specialCharsAll = false,          
            min3 = "",max3 = "",dtype1 = "",dtype2 = "",
            mykey = "RLBS",s1 = true,s2 = true,mobile_otp_check = 0,
            min1 = "1",max1 = "100",min2 = "1",max2 = "2";
            
 function valid() {
                var user_type_pattern = "^[a-zA-Z_ ]*$";
                var pass_type_pattern = "^[0-9a-zA-Z@]*$";
                var num_pattern = "^[0-9]*$";
                var String = "^[a-zA-Z]*$";
                var date_pattern ="^[0-9]{4}[-]?[//]?[0-9]{2}[-]?[//]?[0-9]{2}$";                
                var file_type_pattern = "^[|a-zA-Z@]*$";
				var Text_area="^[0-9a-zA-Z ,.*-/()&$#@(\"\)' ]+$";
                
                a1 = document.getElementById('form_name').value; 

                if (s1 == true) {
                    if (a1 === "" || a1 === null)
                    {
                        alert("Please Enter New Form Name");
                        document.getElementById('form_name').focus();
                        return false;
                    }
                    else if (!a1.match(Text_area))
                    {
                        alert("Enter  Valid Form Name");
                        document.getElementById('form_name').focus();
                        return false;
                    } else if (a1.length > max1 || a1.length < min1) {
                        alert("Length Exceeded");
                        document.getElementById('form_name').focus();
                        return false;
                    } 
					/*else {
                        for (x = 0; x < a1.length; x++)
                        {
                            var c = a1.charAt(x);
                            if ((c >= 'a') && (c <= 'z')) {
                                text = true;
                            }
                            if ((c >= 'A') && (c <= 'Z')) {
                                text1 = true;
                            }

                        }
                        if (text === "false" || text1 == "false")
                        {
                            alert("FormName should be Enter Characters only");
                            document.getElementById('form_name').focus();
                            return false;
                        }
                    }*/
                } else {
                }
                a2 = document.getElementById('fields').value;
                if (s2 == true) {
                    if (a2 == "" || a2 == null)
                    {
                        alert("Please Enter  No Of Fields");
                        document.getElementById('fields').focus();
                        return false;
                    }
                    else if (!a2.match(num_pattern))
                    {
                        alert("Enter  Valid No Of Fields");
                        document.getElementById('fields').focus();
                        return false;
                    } else if (a2.length > max2 || a2.length < min2) {
                        alert("Length Exceeded");
                        document.getElementById('fields').focus();
                        return false;
                    } else {
                        for (x = 0; x < a2.length; x++)
                        {
                            var c = a2.charAt(x);
                            if ((c >= '0') && (c <= '9')) {
                                numeric = true;
                            }

                        }
                        if (numeric == false)
                        {
                            alert("No Of Fields should be Enter Numbers only");
                            document.getElementById('fields').focus();
                            return false;
                        }
                    }

                } else {
                }

                var jsonObj1 = {"formname": a1, "fields": a2};
                arr[0] = jsonObj1; 
                var j = 0;
                for (i = 1; i <= a2; i++) {
                    var jsonObj = "";
                    a3 = document.getElementById('label_' + i).value;

                    if (a3 == "" || a3 == null)
                    {
                        alert("Please Enter  Label Name");
                        document.getElementById('label_' + i).focus();
                        return false;
                    }
                    else {
                        if (!a3.match(user_type_pattern))
                        {
                            alert("Enter  Valid Label Name");
                            document.getElementById('label_' + i).focus();
                            return false;
                        }
                    }

                    a4 = document.getElementById('dtype_' + i).value;
                    if (a4 == "" || a4 == null)
                    {
                        alert("Please Select  Data Type");
                        document.getElementById('dtype_' + i).focus();
                        return false;
                    }
                    else {
                        if (!a4.match(user_type_pattern))
                        {
                            alert("Enter  Valid Data Type");
                            document.getElementById('dtype_' + i).focus();
                            return false;
                        }
                    }
					if(a4 == "mobile"){
					mobile_otp_check = 1;					 	
					}
					
                    a5 = document.getElementById('minsize_' + i).value;
                    if (a5 == "" || a5 == null)
                    {
                        alert("Please Enter  Min Size");
                        document.getElementById('minsize_' + i).focus();
                        return false;
                    }
                    else {
                        if (!a5.match(num_pattern))
                        {
                            alert("Enter  Valid Min Size");
                            document.getElementById('minsize_' + i).focus();
                            return false;
                        }
                    }

                    a6 = document.getElementById('maxsize_' + i).value;
                    if (a6 == "" || a6 == null)
                    {
                        alert("Please Enter  Max Size");
                        document.getElementById('maxsize_' + i).focus();
                        return false;
                    } else if (!a6.match(num_pattern)) {
                        alert("Enter  Valid Max Size");
                        document.getElementById('maxsize_' + i).focus();
                        return false;
                    }

                    a7 = document.getElementById('mandatory_' + i).value;
                    if (a7 == "" || a7 == null)
                    {
                        alert("Please Select Mandatory");
                        document.getElementById('mandatory_' + i).focus();
                        return false;
                    }
                    else {
                        if (!a7.match(String))
                        {
                            alert("Enter  Valid Mandatory");
                            document.getElementById('mandatory_' + i).focus();
                            return false;
                        }
                    }                   
                    
              
                 if (a4 == "dropdown") {
                 a10 = document.getElementById('no1_' + i).value;  
                    if (a10 == "" || a10 == null || parseInt(a10)==0)
                    {
                        alert("No of Drop Values can't be blank or zero");
                        document.getElementById('no1_' + i).focus();
                        return false;
                    }
                    else {
                        if (!a10.match(num_pattern))
                        {
                            alert("Enter  Valid No of Drop Values");
                            document.getElementById('no1_' + i).focus();
                            return false;
                        }
                    }                   
                   
                   for (j = 0; j < a10; j++) {
                var a11 = document.getElementById('dropvalues_'+ i  + j).value;
                    if (a11 == "" || a11 == null)
                    {
                        alert("Please Enter  Drop Value");
                        document.getElementById('dropvalues_' + i + j).focus();
                        return false;
                    }else {
                        if (!a11.match(user_type_pattern))
                        {
                            alert("Enter  Valid  Drop Value");
                            document.getElementById('dropvalues_' + i + j).focus();
                            return false;
                        }else if (a11.length > a6 || a11.length < a5) {
                        alert("Drop Value Min Length  '"+a5+"' and Max Length '"+a6+"'");
                        document.getElementById('dropvalues_' + i + j).value = "";
                        return false;
                        }
                    }
                    arr1[j] = a11;   
                }
//alert("===="+JSON.stringify(arr1));
                //jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "multicolumn": a9, "no": a10, "dropvalues": arr1, "radiobutton": a12, "checkbox": a15, "textarea": a18, "password": a19, "email": a20, "date": a21, "file": a22};
//                jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "multicolumn": a9, "no": a10, "dropvalues": arr1};
                   jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "no1": a10, "dropvalues": arr1};
                    arr[i] = jsonObj; 
                    arr1 = [];
                    
                } else {  
                  if (a4 == "radiobutton") {
//                 if (a12 == true) {
                 a13 = document.getElementById('no2_' + i).value;                 
                    if (a13 == "" || a13 == null || parseInt(a13)==0)
                    {
                        alert("No of Radio Values can't be blank or zero");
                        document.getElementById('no2_' + i).focus();
                        return false;
                    }
                    else {
                        if (!a13.match(num_pattern))
                        {
                            alert("Enter  Valid No of Radio Values");
                            document.getElementById('no2_' + i).focus();
                            return false;
                        }
                    }                   
                   
                   for (k = 0; k < a13; k++) {
                var a14 = document.getElementById('radiovalues_'+ i  + k).value;
                    if (a14 == "" || a14 == null)
                    {
                        alert("Please Enter  Radio Value");
                        document.getElementById('radiovalues_' + i + k).focus();
                        return false;
                    }
                    else {
                        if (!a14.match(String))
                        {
                            alert("Enter  Valid  Radio Value");
                            document.getElementById('radiovalues_' + i + k).focus();
                            return false;
                        }else if (a14.length > a6 || a14.length < a5) {
                        alert("Radio Value Min Length  '"+a5+"' and Max Length '"+a6+"'");
                        document.getElementById('radiovalues_' + i + k).value = "";
                        return false;
                        }
                    }
                    arr1[k] = a14;   
                }
//alert("===="+JSON.stringify(arr1));
                //jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "multicolumn": a9, "radiobutton": a12, "number": a13, "radiovalues": arr1, "checkbox": a15, "textarea": a18, "password": a19 , "email": a20, "date": a21, "file": a22};
//                jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8,"radiobutton": a12, "number": a13, "radiovalues": arr1};
                   jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "no2": a13, "radiovalues": arr1};
                    arr[i] = jsonObj; 
                    arr1 = [];
                }   else {
                	if (a4 == "checkbox") {
//                if (a15 == true) {
                 a16 = document.getElementById('no3_' + i).value;                 
                    if (a16 == "" || a16 == null || parseInt(a16)==0)
                    {
                        alert("No of Check Values can't be blank or zero");
                        document.getElementById('no3_' + i).focus();
                        return false;
                    }
                    else {
                        if (!a16.match(num_pattern))
                        {
                            alert("Enter  Valid No of Check Values");
                            document.getElementById('no3_' + i).focus();
                            return false;
                        }
                    }                   
                   
                   for (m = 0; m < a16; m++) {
                var a17 = document.getElementById('checkvalues_'+ i  + m).value;
                    if (a17 == "" || a17 == null)
                    {
                        alert("Please Enter  Check Value");
                        document.getElementById('checkvalues_' + i + m).focus();
                        return false;
                    }
                    else {
                        if (!a17.match(String))
                        {
                            alert("Enter  Valid  Check Value");
                            document.getElementById('checkvalues_' + i + m).focus();
                            return false;
                        }else if (a17.length > a6 || a17.length < a5) {
                        alert("Check Value Min Length  '"+a5+"' and Max Length '"+a6+"'");
                        document.getElementById('checkvalues_' + i + m).value = "";
                        return false;
                        }
                    }
                    arr1[m] = a17;   
                }
//alert("===="+JSON.stringify(arr1));
                //jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "multicolumn": a9, "radiobutton": a12, "checkbox": a15, "numb": a16, "checkvalues": arr1, "textarea": a18, "password": a19 , "email": a20, "date": a21, "file": a22};
//                jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "checkbox": a15, "numb": a16, "checkvalues": arr1};
                   jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "no3": a16, "checkvalues": arr1};
                    arr[i] = jsonObj; 
                    arr1 = [];
                } else{
                if(a4 == "file"){
                	var file_type = document.getElementById('file_type' + i).value;                 
                    if (file_type == "" || file_type == null)
                    {
                        alert("Please Select File Type like Pdf/Jpg/Png/Jpeg");
                        document.getElementById('file_type' + i).focus();
                        return false;
                    }
                    else {
                        if (!file_type.match(file_type_pattern))
                        {
                            alert("Select Valid File Type");
                            document.getElementById('file_type' + i).focus();
                            return false;
                        }
                    }
                    
                    jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7,  "file_type": file_type};
                    arr[i] = jsonObj;
                }else{
                jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7};
                arr[i] = jsonObj;
                }
           }
           }
           }
           }
                
                var count = document.getElementById("fields").value;
                var countValue = false, j = 0, m = "";
                for (j = 0; j < count; j++) {
                    if (document.getElementById('con_check' + (j + 1)).checked == true) {
                        countValue = true;
                        if (m == "") {
                            m = document.getElementById('dtype_' + (j + 1)).value;
                        } else {
                            m = m + "||" + document.getElementById('dtype_' + (j + 1)).value;
                        }   
                    }
                }
                if (!countValue) {
                    alert("Please Select Atleast one Field as a Constraint");
                    return false;
                } 
                document.getElementById("con_value").value = m;
                
                a30 = document.getElementById('from_date').value; 
                if (a30 == "" || a30 == null)
                    {
                        alert("Please Select From Date ");
                        document.getElementById('from_date').focus();
                        return false;
                    }  
                    else if (!a30.match(date_pattern))
                    {
                        alert("Select  Valid From Date ");
                        document.getElementById('from_date').focus();
                        return false;
                    } else {
                        if (a30.length > 10 || a30.length < 10) {
                        alert("Length Exceeded==" +a30);
                        document.getElementById('from_date').focus();
                        return false;
                    }
                   }
                
                a31 = document.getElementById('to_date').value;     
                if (a31 == "" || a31 == null)
                    {
                        alert("Please Select To Date ");
                        document.getElementById('to_date').focus();
                        return false;
                    }  
                    else if (!a31.match(date_pattern))
                    {
                        alert("Select  Valid To Date ");
                        document.getElementById('to_date').focus();
                        return false;
                    } else {
                        if (a31.length > 10 || a31.length < 10) {
                        alert("Length Exceeded==" +a31);
                        document.getElementById('to_date').focus();
                        return false;
                    }
                   }
                   
				    var currentDate = document.getElementById('currentDate').value;
					var to_date = document.getElementById('to_date').value;	
					var from_date = document.getElementById('from_date').value;
					let date1 = new Date(currentDate).getTime();
				    let date2 = new Date(to_date).getTime();
				    let date3 = new Date(from_date).getTime();
				    
				    if (date2 < date3) {
				    alert("Please select Expiry To Date after Expiry From Date");
				    return false;    
				   } else if (date2 > date3) {    
				  	} else {
				    alert("Expiry From and Expiry To dates are equal");
				    return false;
				  }
				    
					if (date1 < date2) {    
				   } else if (date1 > date2) {
				    alert("Please select To Date after Current Date")
				    return false;
				  } else {
				    alert("Current Date and Expiry To dates are equal");
				    return false;
				  }   
                
                var selchbox2 = "",selchbox3 = "";
                var a32 = document.getElementsByName('captcha');  
                var h= a32.length;
                var p1 = 0;
                for(p1 = 0; p1<h; p1++) {
                    if(a32[p1].type == 'radio' && a32[p1].checked == true){ 
                    selchbox2 = a32[p1].value;      
                   }      
                  }                                                                                               
                    if (selchbox2 == "" || selchbox2 == null)
                    {
                        alert("Please select Captcha Yes/No ");
                        return false;
                    }  
                    else if (!selchbox2.match(String))
                    {
                        alert("Select  Valid Captcha Yes/No");
                        return false;
                    } else if (selchbox2.length > 1 || selchbox2.length < 1) {
                        alert("Length Exceeded Captcha===");
                        return false;
                    }

			    var a33 = document.getElementsByName('sms');  
                var hh= a33.length;
                var p2 = 0;
                for(p2 = 0; p2<hh; p2++) {
                    if(a33[p2].type == 'radio' && a33[p2].checked == true){ 
                    selchbox3 = a33[p2].value;      
                   }      
                  }                                                                                               
                    if (selchbox3 == "" || selchbox3 == null)
                    {
                        alert("Please select OTP Yes/No ");
                        return false;
                    }  
                    else if (!selchbox3.match(String))
                    {
                        alert("Select  Valid OTP Yes/No");
                        return false;
                    } else if (selchbox3.length > 1 || selchbox3.length < 1) {
                        alert("Length Exceeded OTP===");
                        return false;
                    }
                    
                    if(mobile_otp_check == 0){
					 alert("Please take one Field as a Mobile");
					 return false;	
					}
                
                
                var jsonString = JSON.stringify(arr);

                    var jsonCheck = mykey + SHA256(jsonString);                    
                    document.getElementById("jsonCheck").value = jsonCheck;
                    document.getElementById("jsonData").value = jsonString;                   
                
 }           

 function valid_form() {
     var user_type_pattern = "^[a-zA-Z_ ]*$";
     var pass_type_pattern = "^[0-9a-zA-Z@]*$";
     var num_pattern = "^[0-9]*$";
     var String = "^[a-zA-Z]*$";
     var date_pattern ="^[0-9]{4}[-]?[//]?[0-9]{2}[-]?[//]?[0-9]{2}$";
     var file_type_pattern = "^[|a-zA-Z@]*$";

     a1 = document.getElementById('form_id').value;                
     if (s1 == true) {
         if (a1 == "" || a1 == null)
         {
             alert("Please Select Form Name");
             return false;
         }
         else if (!a1.match(num_pattern))
         {
             alert("Select  Valid Form Name");
             return false;
         } else if (a1.length > max1 || a1.length < min1) {
             alert("Length Exceeded");
             return false;
         } else{}
     } else {
     }
     a2 = document.getElementById('fields').value;
     if (s2 == true) {
         if (a2 == "" || a2 == null)
         {
             alert("Please Enter  No Of Fields");
             return false;
         }
         else if (!a2.match(num_pattern))
         {
             alert("Enter  Valid No Of Fields");
             return false;
         } else if (a2.length > max2 || a2.length < min2) {
             alert("Length Exceeded");
             return false;
         } else {
             for (x = 0; x < a2.length; x++)
             {
                 var c = a2.charAt(x);
                 if ((c >= '0') && (c <= '9')) {
                     numeric = true;
                 }

             }
             if (numeric == false)
             {
                 alert("No Of Fields should be Enter Numbers only");
                 return false;
             }
         }

     } else {
     }

     var form_name = document.getElementById("form_id").options[document.getElementById("form_id").selectedIndex].text;
     document.getElementById("form_name").value = form_name;

     var jsonObj1 = {"formname": form_name, "fields": a2};
     arr[0] = jsonObj1;               
     for (i = 1; i <= a2; i++) {

         var jsonObj = "";
         a3 = document.getElementById('label_' + i).value;

         if (a3 == "" || a3 == null)
         {
             alert("Please Enter  Label Name");
             document.getElementById('label_' + i).focus();
             return false;
         }
         else {
             if (!a3.match(user_type_pattern))
             {
                 alert("Enter  Valid Label Name");
                 document.getElementById('label_' + i).focus();
                 return false;
             }
         }

         a4 = document.getElementById('dtype_' + i).value;
         
         if (a4 == "" || a4 == null)
         {
             alert("Please Select  Data Type");
             document.getElementById('dtype_' + i).focus();
             return false;
         }
         else {
             if (!a4.match(user_type_pattern))
             {
                 alert("Enter  Valid Data Type");
                 document.getElementById('dtype_' + i).focus();
                 return false;
             }
         }

         a5 = document.getElementById('minsize_' + i).value;
                 
         if (a5 == "" || a5 == null)
         {
             alert("Please Enter  Min Size");
             document.getElementById('minsize_' + i).focus();
             return false;
         }
         else {
             if (!a5.match(num_pattern))
             {
                 alert("Enter  Valid Min Size");
                 document.getElementById('minsize_' + i).focus();
                 return false;
             }
         }

         a6 = document.getElementById('maxsize_' + i).value;
                 
         if (a6 == "" || a6 == null)
         {
             alert("Please Enter  Max Size");
             document.getElementById('maxsize_' + i).focus();
             return false;
         } else if (!a6.match(num_pattern)) {
             alert("Enter  Valid Max Size");
             document.getElementById('maxsize_' + i).focus();
             return false;
         }

         a7 = document.getElementById('mandatory_' + i).value;
                  
         if (a7 == "" || a7 == null)
         {
             alert("Please Select Mandatory");
             document.getElementById('mandatory_' + i).focus();
             return false;
         }
         else {
             if (!a7.match(String))
             {
                 alert("Enter  Valid Mandatory");
                 document.getElementById('mandatory_' + i).focus();
                 return false;
             }
         }                                   


     if (a4 == "dropdown") {
      a10 = document.getElementById('no1_' + i).value;  
         if (a10 == "" || a10 == null || parseInt(a10)==0)
         {
             alert("Drop Values can't be blank or zero");
             document.getElementById('no1_' + i).focus();
             return false;
         }
         else {
             if (!a10.match(num_pattern))
             {
                 alert("Enter  Valid No of Drop Values");
                 document.getElementById('no1_' + i).focus();
                 return false;
             }
         }                   
        
        for (j = 0; j < a10; j++) {
     var a11 = document.getElementById('dropvalues_'+ i  + j).value;
         if (a11 == "" || a11 == null)
         {
             alert("Please Enter  Drop Value");
             document.getElementById('dropvalues_' + i + j).focus();
             return false;
         }
         else {
             if (!a11.match(user_type_pattern))
             {
                 alert("Enter  Valid  Drop Value");
                 document.getElementById('dropvalues_' + i + j).focus();
                 return false;
             }else if (a11.length > a6 || a11.length < a5) {
                        alert("Drop Value Min Length  '"+a5+"' and Max Length '"+a6+"'");
                        document.getElementById('dropvalues_' + i + j).value = "";
                        return false;
                        }
         }
         arr1[j] = a11;   
     }
//alert("===="+JSON.stringify(arr1));
     //jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "multicolumn": a9, "no": a10, "dropvalues": arr1, "radiobutton": a12, "checkbox": a15, "textarea": a18, "password": a19, "email": a20, "date": a21, "file": a22};
     jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "no1": a10, "dropvalues": arr1};
         arr[i] = jsonObj; 
         arr1 = [];
         
     } else {  
      if (a4 == "radiobutton") {
      a13 = document.getElementById('no2_' + i).value; 
         if (a13 == "" || a13 == null || parseInt(a13)==0)
         {
             alert("Radio Values can't be blank or zero");
             document.getElementById('no2_' + i).focus();
             return false;
         }
         else {
             if (!a13.match(num_pattern))
             {
                 alert("Enter  Valid No of Radio Values");
                 document.getElementById('no2_' + i).focus();
                 return false;
             }
         }                   
        
        for (k = 0; k < a13; k++) {
     var a14 = document.getElementById('radiovalues_'+ i  + k).value;
         if (a14 == "" || a14 == null)
         {
             alert("Please Enter  Radio Value");
             document.getElementById('radiovalues_' + i + k).focus();
             return false;
         }
         else {
             if (!a14.match(String))
             {
                 alert("Enter  Valid  Radio Value");
                 document.getElementById('radiovalues_' + i + k).focus();
                 return false;
             }else if (a14.length > a6 || a14.length < a5) {
                        alert("Radio Value Min Length  '"+a5+"' and Max Length '"+a6+"'");
                        document.getElementById('radiovalues_' + i + k).value = "";
                        return false;
                        }

         }
         arr1[k] = a14;   
     }
//alert("===="+JSON.stringify(arr1));
     //jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "multicolumn": a9, "radiobutton": a12, "number": a13, "radiovalues": arr1, "checkbox": a15, "textarea": a18, "password": a19 , "email": a20, "date": a21, "file": a22};
     jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "no2": a13, "radiovalues": arr1};
         arr[i] = jsonObj; 
         arr1 = [];
     }   else {
     
     if (a4 == "checkbox") {
      a16 = document.getElementById('no3_' + i).value; 
         if (a16 == "" || a16 == null || parseInt(a16)==0)
         {
             alert("Check Values can't be blank or zero");
             document.getElementById('no3_' + i).focus();
             return false;
         }
         else {
             if (!a16.match(num_pattern))
             {
                 alert("Enter  Valid No of Check Values");
                 document.getElementById('no3_' + i).focus();
                 return false;
             }
         }                   
        
        for (m = 0; m < a16; m++) {
     var a17 = document.getElementById('checkvalues_'+ i  + m).value;
         if (a17 == "" || a17 == null)
         {
             alert("Please Enter  Check Value");
             document.getElementById('checkvalues_' + i + m).focus();
             return false;
         }
         else {
             if (!a17.match(String))
             {
                 alert("Enter  Valid  Check Value");
                 document.getElementById('checkvalues_' + i + m).focus();
                 return false;
             }else if (a17.length > a6 || a17.length < a5) {
                        alert("Check Value Min Length  '"+a5+"' and Max Length '"+a6+"'");
                        document.getElementById('checkvalues_' + i + m).value = "";
                        return false;
             }
         }
         arr1[m] = a17;   
     }
//alert("===="+JSON.stringify(arr1));
     //jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7, "format": a8, "multicolumn": a9, "radiobutton": a12, "checkbox": a15, "numb": a16, "checkvalues": arr1, "textarea": a18, "password": a19 , "email": a20, "date": a21, "file": a22};
     jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7,  "no3": a16, "checkvalues": arr1};
         arr[i] = jsonObj; 
         arr1 = [];
     } else{
    	 if(a4 == "file"){
         	var file_type = document.getElementById('file_type' + i).value;  
             if (file_type == "" || file_type == null)
             {
                 alert("Please Select File Type like Pdf/Jpg/Png/Jpeg");
                 document.getElementById('file_type' + i).focus();
                 return false;
             }
             else {
                 if (!file_type.match(file_type_pattern))
                 {
                     alert("Select Valid File Type");
                     document.getElementById('file_type' + i).focus();
                     return false;
                 }
             }
             jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7,  "file_type": file_type};
             arr[i] = jsonObj;
         }else{
    	   jsonObj = {"label": a3, "dtype": a4, "minsize": a5, "maxsize": a6, "mandatory": a7};    
    	   arr[i] = jsonObj;
     }

}}}}
     
     var count = document.getElementById("fields").value;
     var countValue = false, j = 0, m = "";
     for (j = 0; j < count; j++) {
         if (document.getElementById('con_check' + (j + 1)).checked == true) {
             countValue = true;
             if (m == "") {
                 m = document.getElementById('dtype_' + (j + 1)).value;
             } else {
                 m = m + "||" + document.getElementById('dtype_' + (j + 1)).value;
             }   
         }
     }
     if (!countValue) {
         alert("Please Select Atleast one Field as a Constraint");
         return false;
     } 
     

     document.getElementById("con_value").value = m;
     
     a30 = document.getElementById('from_date').value; 
     if (a30 == "" || a30 == null)
         {
             alert("Please Select From Date ");
             document.getElementById('from_date').focus();
             return false;
         }  
         else if (!a30.match(date_pattern))
         {
             alert("Select  Valid From Date ");
             document.getElementById('from_date').focus();
             return false;
         } else {
             if (a30.length > 10 || a30.length < 10) {
             alert("Length Exceeded==" +a30);
             document.getElementById('from_date').focus();
             return false;
         }
        }
     
     a31 = document.getElementById('to_date').value;     
     if (a31 == "" || a31 == null)
         {
             alert("Please Select To Date ");
             document.getElementById('to_date').focus();
             return false;
         }  
         else if (!a31.match(date_pattern))
         {
             alert("Select  Valid To Date ");
             document.getElementById('to_date').focus();
             return false;
         } else {
             if (a31.length > 10 || a31.length < 10) {
             alert("Length Exceeded==" +a31);
             document.getElementById('to_date').focus();
             return false;
         }
        }
        
        var currentDate = document.getElementById('currentDate').value;
	var to_date = document.getElementById('to_date').value;	
	var from_date = document.getElementById('from_date').value;
	let date1 = new Date(currentDate).getTime();
    let date2 = new Date(to_date).getTime();
    let date3 = new Date(from_date).getTime();
    
    if (date2 < date3) {
    alert("Please select Expiry To Date after Expiry From Date");
    return false;    
   } else if (date2 > date3) {    
  	} else {
    alert("Expiry From and Expiry To dates are equal");
    return false;
  }
    
	if (date1 < date2) {    
   } else if (date1 > date2) {
    alert("Please select To Date after Current Date")
    return false;
  } else {
    alert("Current Date and Expiry To dates are equal");
    return false;
  } 
     
     var selchbox2 = "";
     var a32 = document.getElementsByName('captcha');  
     var h = a32.length;
     var x2 = 0,p1 = 0;;
     for(p1 = 0; p1<h; p1++) {
         if(a32[p1].type == 'radio' && a32[p1].checked == true){ 
         selchbox2 = a32[p1].value;      
        }      
       }                                                                                               
         if (selchbox2 == "" || selchbox2 == null)
         {
             alert("Please select Captcha Yes/No ");
             return false;
         }  
         else if (!selchbox2.match(String))
         {
             alert("Select  Valid Captcha Yes/No");
             return false;
         } else if (selchbox2.length > 1 || selchbox2.length < 1) {
             alert("Length Exceeded Captcha===");
             return false;
         }

     
     	 var jsonString = JSON.stringify(arr);      
         var jsonCheck = mykey + SHA256(jsonString);                    
         document.getElementById("jsonCheck").value = jsonCheck;
         document.getElementById("jsonData").value = jsonString;                                                                                                               
     
 }           

 function getShowTypeEdit(no, num) {  	  
    if (no =='text' || no == "textarea" || no == "password" || no == "email" || no == "date" || no == "mobile" || no == "panno" || no == "aadhaarno" || no == "number" || no == "specialcombo" || no == "time") {
   	  var table = "";   
       	  
       document.getElementById("sss_" + num).style.display = 'none';
       document.getElementById("ssr_" + num).style.display = 'none';   
       document.getElementById("nnn_" + num).style.display = 'none'; 
       document.getElementById('vvv_' + num).style.display = 'none';
   	   document.getElementById('vvv_' + num).innerHTML = table;       

       document.getElementById("rrr_" + num).style.display = 'none';
       document.getElementById("bbb_" + num).style.display = 'none';   
       document.getElementById("rr_" + num).style.display = 'none'; 
       document.getElementById('bb_' +num).style.display = 'none';
       document.getElementById('bb_'+num).innerHTML = table;       

       document.getElementById("ccc_" + num).style.display = 'none';
       document.getElementById("cb_" + num).style.display = 'none';   
       document.getElementById("cc_" + num).style.display = 'none'; 
       document.getElementById('cbv_'+ num).style.display = 'none';
       document.getElementById('cbv_'+ num).innerHTML = table;

           document.getElementById("ff2_" + num).style.display = 'none';
           document.getElementById("ff3_" + num).style.display = 'none';          
      } else if (no == "dropdown") {
       document.getElementById("sss_" + num).style.display = 'block';
       document.getElementById("ssr_" + num).style.display = 'block';     

       var table = "";

           document.getElementById("rrr_" + num).style.display = 'none';
           document.getElementById("bbb_" + num).style.display = 'none';   
           document.getElementById("rr_" + num).style.display = 'none'; 
           document.getElementById('bb_' +num).style.display = 'none';

           document.getElementById("ccc_" + num).style.display = 'none';
           document.getElementById("cb_" + num).style.display = 'none';   
           document.getElementById("cc_" + num).style.display = 'none'; 
           document.getElementById('cbv_'+ num).style.display = 'none';
           
           document.getElementById("ff2_" + num).style.display = 'none';
           document.getElementById("ff3_" + num).style.display = 'none';           
          
      } else if (no == "radiobutton") {
       document.getElementById("rrr_" + num).style.display = 'block';
       document.getElementById("bbb_" + num).style.display = 'block';        

       var table = "";
           document.getElementById("sss_" + num).style.display = 'none';
           document.getElementById("ssr_" + num).style.display = 'none';   
           document.getElementById("nnn_" + num).style.display = 'none'; 
           document.getElementById('vvv_' + num).style.display = 'none';

           document.getElementById("ccc_" + num).style.display = 'none';
           document.getElementById("cb_" + num).style.display = 'none';   
           document.getElementById("cc_" + num).style.display = 'none'; 
           document.getElementById('cbv_'+ num).style.display = 'none';
          
           document.getElementById("ff2_" + num).style.display = 'none';
           document.getElementById("ff3_" + num).style.display = 'none';          
      } else if (no == "checkbox") {
       document.getElementById("ccc_" + num).style.display = 'block';
       document.getElementById("cb_" + num).style.display = 'block';       
       
       var table = "";
       document.getElementById('vvv_' + num).innerHTML = table;
       document.getElementById('bb_'+num).innerHTML = table;
       
           document.getElementById("sss_" + num).style.display = 'none';
           document.getElementById("ssr_" + num).style.display = 'none';   
           document.getElementById("nnn_" + num).style.display = 'none'; 
           document.getElementById('vvv_' + num).style.display = 'none';

           document.getElementById("rrr_" + num).style.display = 'none';
           document.getElementById("bbb_" + num).style.display = 'none';   
           document.getElementById("rr_" + num).style.display = 'none'; 
           document.getElementById('bb_' +num).style.display = 'none';
           
           document.getElementById("ff2_" + num).style.display = 'none';
           document.getElementById("ff3_" + num).style.display = 'none';
          
             
      }else if (no == "file") { 
      var table = "";     
       document.getElementById("ff2_" + num).style.display = 'block';
       document.getElementById("ff3_" + num).style.display = 'block';
         
       document.getElementById('vvv_' + num).innerHTML = table;
       document.getElementById('bb_'+num).innerHTML = table;
       document.getElementById('cbv_'+ num).innerHTML = table;       
       
           document.getElementById("sss_" + num).style.display = 'none';
           document.getElementById("ssr_" + num).style.display = 'none';   
           document.getElementById("nnn_" + num).style.display = 'none'; 
           document.getElementById('vvv_' + num).style.display = 'none';
       
           document.getElementById("rrr_" + num).style.display = 'none';
           document.getElementById("bbb_" + num).style.display = 'none';   
           document.getElementById("rr_" + num).style.display = 'none'; 
           document.getElementById('bb_' +num).style.display = 'none';
       
           document.getElementById("ccc_" + num).style.display = 'none';
           document.getElementById("cb_" + num).style.display = 'none';   
           document.getElementById("cc_" + num).style.display = 'none'; 
           document.getElementById('cbv_'+ num).style.display = 'none';
             
      } else{          
      }                                   
 }
  
   function show(no) {  	
      var table = "<table  align='center' width='50%' height='70px' >";
      var j =1;
      for (var i = 1; i <= no; i++) {
          table = table + "<div class='row'><h6 class='text-decoration-underline'>Field " + i + "</h6></div><div class='row mb-2 flex-nowrap'> \n"
                  + " <div class='col-2'><div class='form-check'><input class='form-check-input' type='checkbox' name='con_check'id='con_check"+ i +"'> <label class='form-check-label'for='con_check"+ i +"'> Unique </label></div></div> \n"
                  + " <div class='col-2'><div class='floating-label'><input type='text' name='label' required='true' id='label_" + i + "'onkeypress='return ValidateAlpha(event)'onblur='return checkString(this, ' Label Name');' minlength='1' maxlength='50' autocomplete='off' class='form-control'placeholder='Label Name' /> <label for='label_" + i + "'>Label Name</label></div></div>  \n"
                  + " <div class='col-2'><div class='form-group floating-label'><select class='form-select' id='dtype_" + i + "' name='dtype' required='true' autocomplete='off' onchange='getShowType(this.value," + i + ")'><option value=''>Select</option><option value='text'>Text</option><option value='number'>Number</option><option value='dropdown'>Drop down</option><option value='radiobutton'>Radio button</option><option value='checkbox'>Check box</option><option value='textarea'>Text area</option><option value='password'>Password</option><option value='email'>Email</option><option value='date'>Date</option><option value='file'>File upload</option><option value='specialcombo'>Special combo</option><option value='mobile'>Mobile</option><option value='panno'>PAN Number</option><option value='aadhaarno'>Aadhaar Number</option><option value='time'>Time</option></select> <label for='dtype_" + i + "'>Data Type</label></div></div>\n"
                  + " <div class='col-2'><div class='floating-label'><input type='text' name='minsize' id='minsize_" + i + "' required='true' minlength='1' maxlength='3' onkeypress='return isNumber(event)' autocomplete='off' class='form-control' placeholder='Min Size' /><label for='minsize_" + i + "'>Min Size</label></div></div>\n"
                  + " <div class='col-2'><div class='floating-label'><input type='text' name='maxsize' id='maxsize_" + i + "' required='true' minlength='1' maxlength='3' onkeypress='return isNumber(event)' autocomplete='off' class='form-control' placeholder='Max Size' /> <label for='maxsize_" + i + "'>Max Size</label></div></div> \n"
                  + " <div class='col-2'><div class='form-group floating-label'><select class='form-select' name='mandatory' id='mandatory_" + i + "' required='true' ><option value=''>Select</option><option value='true'>True</option><option value='false'>False</option></select> <label for='mandatory_" + i + "'>Mandatory</label></div></div>\n"                                   
                  + " <div class='col-2' id='sss_" + i + "' style='display:none;'><div class='floating-label' id='ssr_" + i + "' style='display:none;'><input type='text' name='no1' id='no1_" + i + "' minlength='1' maxlength='2'onkeypress='return isNumber(event)' onchange ='getShow1(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no1_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='nnn_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Dropdown Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='vvv_" + i + "'></div></div></div></div>\n"                  
                  + " <div class='col-2' id='rrr_" + i + "' style='display:none;'><div class='floating-label'id='bbb_" + i + "' style='display:none;'><input type='text' name='no2' id='no2_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow3(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no2_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='rr_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Radio button Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='bb_" + i + "'></div></div></div></div>\n"                                    
                  + " <div class='col-2' id='ccc_" + i + "' style='display:none;'><div class='floating-label'id='cb_" + i + "' style='display:none;'><input type='text' name='no3'  id='no3_" + i + "' minlength='1' maxlength='2' onkeypress='return isNumber(event)' onchange ='getShow5(this.value," + i + ")' autocomplete='off' class='form-control'placeholder='No of Values' /> <label for='no3_" + i + "'>No of values</label></div></div> \n"
                  + " <div class='col-2' id='cc_" + i + "' style='display:none;'><div class='dropdown'><button class='btn btn-outline-secondary dropdown-toggle w-100 show' type='button' id='dropdownMenuButton' data-bs-toggle='dropdown' aria-haspopup='true' aria-expanded='true'>Check box Values</button><div class='dropdown-menu w-100 show' aria-labelledby='dropdownMenuButton'><div id='cbv_" + i + "'></div></div></div></div>\n"                                    
                  + " <div class='col-2' id='ff2_" + i + "' style='display:none;'><div class='form-group floating-label' id='ff3_" + i + "'><select class='form-select' name='file_type' id='file_type"+ i +"' ><option value=''>Select</option><option value='pdf'>PDF</option> <option value='jpg|jpeg|png'>JPG|JPEG|PNG</option></select> <label for='file_type"+ i +"' >File Format</label></div></div>\n"
                  + " </div>";
                  
                 
      }
    table = table + "<div class='row'><h6 class='text-decoration-underline mb-3'>Form Settings</h6>\n"
    + " <div class='col-2'><div class='form-group floating-label'><input type='date' name='from_date' id='from_date' class='form-control' placeholder='From date' /> <label for='date'>Expiry From Date</label></div></div> \n"
    + " <div class='col-2'><div class='form-group floating-label'><input type='date' name='to_date' id='to_date' class='form-control' placeholder='To date' /> <label for='date'>Expiry To Date</label></div></div> \n"    
    + " <div class='col-3'><label>Captcha required?</label><div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='captcha' id='captcha1' value='Y'> <label class='btn btn-outline-primary' for='captcha1'>Yes</label> <input type='radio' class='btn-check' name='captcha' id='captcha2' value='N'> <label class='btn btn-outline-primary' for='captcha2'>No</label></div></div>\n"
    + " <div class='col-5'><label>OTP Required to submit this form?</label><div class='btn-group btn-group-sm' role='group'><input type='radio' class='btn-check' name='sms' id='sms1' value='Y'> <label class='btn btn-outline-primary' for='sms1'>Yes</label> <input type='radio' class='btn-check' name='sms' id='sms2' value='N'> <label class='btn btn-outline-primary' for='sms2'>No</label></div></div> \n"
    + " </div>\n"
    //+ "<h6 class='text-decoration-underline m-3  text-danger'>NOTE:</h6><p class='m-0'>Note1</p><p class='m-0'>Note2</p><p class='m-0'>Note3</p>";
   table = table + "</table>";
      document.getElementById('ssr').innerHTML = table;
  }
  
  function json_valid(array) {                                
           var a1 = "", b1 = "", c1 = "", d1 = "", e1 = "", arr = [], arr1 = [], label = "", min = "",max = "",dtype = "", mandatory = "";
           var i = 0, jm = 0, x = 0, text = false, text1 = false, numeric = false, specialCharsAll = false;
           var format = "", multicolumn = "", dropvalues = "", radiobutton = "", checkbox = "", textarea = "", password = "", email = "";
           var date = "", file = "", file_type = "";
           var data = "";  

                var user_type_pattern = "^[a-zA-Z ]*$";
                var pass_type_pattern = "^[0-9a-zA-Z@]*$";
                var pan_pattern = "^[A-Z]{5}?[//]?[0-9]{4}?[//]?[A-Z]{1}$";
                var captcha_pattern = "^[0-9]*$";
                var String = "^[a-zA-Z]*$";
//                var date_pattern="^[0-9]{2}[-]?[//]?[0-9]{2}[-]?[//]?[0-9]{4}$";
                var date_pattern="^[0-9]{4}[-]?[//]?[0-9]{2}[-]?[//]?[0-9]{2}$";
                var email_val="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$";
                var email2_val="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Zruna-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                var date ="dd-mm-yyyy";                
                var Text_area="^[0-9a-zA-Z ,.*-/()&$#@(\"\)' ]+$";
                var time_pattern="^[0-9]{2}[:]?[//]?[0-9]{2}$";
                                                                       
                arr1=array; 
              data = JSON.parse(arr1); 
        for (i = 0;i < data.length; i++) {           	
        var jsonObj1 = {};
        var data1 =  data[i];        
//            var jsonString = JSON.stringify(JsonObject);            
//          var  data1 = JSON.parse(jsonString);
        label = data1.label;       
        dtype =  data1.dtype;       
        min = data1.minsize;          
        max = data1.maxsize;          
        mandatory = data1.mandatory;          
        file_type = data1.file_type;
        
                if (mandatory == "true") { 
                if (dtype == "dropdown") { 
                a1 = document.getElementById('dropdown_'+i).value; 
                var x1 = 0;
                    if (a1 == "" || a1 == null)
                    {
                        alert("Please select "+label+" ");                        
                        return false;
                    }  
                    else if (!a1.match(user_type_pattern))
                    {
                        alert("select  Valid "+label+" ");
                        return false;
                    } else if (a1.length > max || a1.length < min) {
                        alert("Length Exceeded==" +a1);
                        return false;
                    } else {
                        for (x1 = 0; x1 < a1.length; x1++)
                        {
                            var c1 = a1.charAt(x1);
                            if ((c1 >= 'a') && (c1 <= 'z')) {
                                text = true;
                            }
                            if ((c1 >= 'A') && (c1 <= 'Z')) {
                                text1 = true;
                            }

                        }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+" should be Enter Characters only");
                            return false;
                        }
                    } 
                        jsonObj1[label] = a1;
                        
                }else if (dtype == "specialcombo") { 
                	    a23 = document.getElementById('stateCode').value;
                        if (a23 == "" || a23 == null)
                        {
                            alert("Please select State ");                        
                            return false;
                        }  
                        else if (!a23.match(captcha_pattern))
                        {
                            alert("select  Valid State ");
                            return false;
                        } else if (a23.length > 5 || a23.length < 1) {
                            alert("Length Exceeded==" +a23);
                            return false;
                        } else {}
                            jsonObj1['stateCode'] = a23;
                		
                        a24 = document.getElementById('distCode').value; 
                            if (a24 == "" || a24 == null)
                            {
                                alert("Please select District ");                        
                                return false;
                            }  
                            else if (!a24.match(captcha_pattern))
                            {
                                alert("select  Valid District ");
                                return false;
                            } else if (a24.length > 5 || a24.length < 1) {
                                alert("Length Exceeded==" +a24);
                                return false;
                            } else {}
                                jsonObj1['distCode'] = a24;  
                      
                                a25 = document.getElementById('mandalCode').value; 
                                    if (a25 == "" || a25 == null)
                                    {
                                        alert("Please select Mandal ");                        
                                        return false;
                                    }  
                                    else if (!a25.match(captcha_pattern))
                                    {
                                        alert("select  Valid Mandal ");
                                        return false;
                                    } else if (a25.length > 10 || a25.length < 1) {
                                        alert("Length Exceeded==" +a25);
                                        return false;
                                    }else{}
                                        jsonObj1['mandalCode'] = a25;       
                                
                                        a26 = document.getElementById('villCode').value; 
                                            if (a26 == "" || a26 == null)
                                            {
                                                alert("Please select Village ");                        
                                                return false;
                                            }  
                                            else if (!a26.match(captcha_pattern))
                                            {
                                                alert("select  Valid Village ");
                                                return false;
                                            } else if (a26.length > 10 || a26.length < 1) {
                                                alert("Length Exceeded==" +a26);
                                                return false;
                                            }else{}
                                                jsonObj1['villCode'] = a26;       
			var villName = document.getElementById("villCode").options[document.getElementById("villCode").selectedIndex].text;
     		document.getElementById("villName").value = villName;
                                                                                                  
        }else if (dtype == "radiobutton") {                  	
                var selchbox1 = "";
                var d = document.getElementsByName('radio_button_'+i);  
                var h= d.length;
                var x2 = 0,p1 = 0;;
                for(p1 = 0; p1<h; p1++) {
                    if(d[p1].type == 'radio' && d[p1].checked == true){ 
                    selchbox1 = d[p1].value;      
                   }      
                  }                                                                                               
                    if (selchbox1 == "" || selchbox1 == null)
                    {
                        alert("Please select "+label+" ");
                        return false;
                    }  
                    else if (!selchbox1.match(String))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else if (selchbox1.length > max || selchbox1.length < min) {
                        alert("Length Exceeded===" +selchbox1);
                        return false;
                    } else {
                        for (x2 = 0; x2 < selchbox1.length; x2++)
                        {
                            var c2 = selchbox1.charAt(x2);
                            if ((c2 >= 'a') && (c2 <= 'z')) {
                                text = true;
                            }
                            if ((c2 >= 'A') && (c2 <= 'Z')) {
                                text1 = true;
                            }

                        }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+" should be Enter Characters only");
                            return false;
                        }
                    }
                        jsonObj1[label] = selchbox1;
        } else if (dtype == "checkbox") {                  	   
                    var selchbox = [];
                    var c3 = document.getElementsByName('check_box_'+i);                     
                    var g = c3.length;
                    var j=0, t1 = 0, k1 = 0,u1 = 0;;
                    for(t1 = 0; t1<g; t1++) {
                    if(c3[t1].type == 'checkbox' && c3[t1].checked == true){ 
                    selchbox[j] = c3[t1].value; 
                    j++;  
                    }      
                    }
                    if(selchbox.length == "0"){
                    alert("Please select "+label+" ");
                    return false;  
                    }else{
                       for(k1 = 0; k1<selchbox.length ;k1++){
                     if (!selchbox[k1].match(String))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else if (selchbox[k1].length > max || selchbox[k1].length < min) {
                        alert("Length Exceeded==" +selchbox[k1]);
                        return false;
                    } else {
                        for (u1 = 0; u1 < selchbox[k1].length; u1++)
                        {
                            var c4 = selchbox[k1].charAt(u1);
                            if ((c4 >= 'a') && (c4 <= 'z')) {
                                text = true;
                            }
                            if ((c4 >= 'A') && (c4 <= 'Z')) {
                                text1 = true;
                            }

                        }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+" should be Enter Characters only");
                            return false;
                        }
                    }
                }
                }
                    jsonObj1[label] = selchbox;                    
        }else if (dtype == "textarea") {                    	
                    var e = document.getElementById('textarea_'+i).value; 
                    var x3 = 0;
                  if (e == "" || e == null)
                    {
                        alert("Please Enter "+label+" ");
                        return false;
                    }  
                    else if (!e.match(Text_area))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else {
                        if (e.length > max || e.length < min) {
                        alert("Length Exceeded==" +e);
                        return false;
                          }
                     }

                    jsonObj1[label] = e;
        } else if (dtype == "password") {                	
                    var f = document.getElementById('password_'+i).value; 
                    var w1 = 0;
                  if (f == "" || f == null)
                    {
                        alert("Please Enter "+label+" ");
                        return false;
                    }   
                    else if (f.length > max || f.length < min) {
                       alert("password should be minimum '"+min+"' and maximum '"+max+"' digit With Combination of Lower Case and Upper Case and Numbers,one Special Symbol(@,#,$,*,&)" +f);
                        return false;
                    } else {
                        for (w1 = 0; w1 < f.length; w1++)
                        {                           
                            var c6 = f.charAt(w1);                             
                            if ((c6 >= 'a') && (c6 <= 'z')) {
                                text = true;
                            }
                            if ((c6 >= 'A') && (c6 <= 'Z')) {
                                text1 = true;
                            }
                            if ((c6 >= '0') && (c6 <= '9')) {
                                numeric = true;
                            }
                            if ((c6 == '*') || (c6 == '#') || (c6 == '&') || (c6 == '@') || (c6 == '$')) {
                                specialCharsAll = true;
                            }
                        }                        
                        if (text == false || text1 == false || numeric == false || specialCharsAll == false)
                        {
                            alert(" "+label+"  should be Enter Combination of Lower Case and Upper Case and Number,one Special Symbol(@,#,$,*,&)");
                            return false;
                        }
                    }
                  var pwd = SHA256(f);                  
                  jsonObj1[label] = pwd;
        } else  if(dtype == "email"){                   	
                 c1 = document.getElementById('email_'+i).value;
                if (c1 == "" || c1 == null)
                    {
                        alert("Please Enter "+label+" ");
                        return false;
                    }  
                    else if (!c1.match(email2_val))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else {
                        if (c1.length > max || c1.length < min) {
                        alert("Length Exceeded==" +c1);
                        return false;
                    }
                   }

                 jsonObj1[label] = c1;    
        } else if(dtype == "date"){                         	
                	 
                 d1 = document.getElementById('date'+x).value;                  
                     
                if (d1 == "" || d1 == null)
                    {
                        alert("Please Enter "+label+" ");
                        return false;
                    }  
                    else if (!d1.match(date_pattern))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else {
                        if (d1.length > max || d1.length < min) {
                        alert("Length Exceeded==" +d1);
                        return false;
                    }
                   }
                 jsonObj1[label] = d1; 
                 x++;
        } else  if(dtype == "file"){				
                 e1 = document.getElementById('file'+jm).value; 
				 if(document.getElementById('flag').value=="insert"){
//               var  e1 = document.getElementsByName('file_'+i); 
                    if (e1 == "" || e1 == null )
                    {                   
                    	alert("Please Upload "+label+" ");
                        return false;
                    }else{
 
              //  var fileName = document.getElementById('file'+jm).files[0].name;
                var allowed_extensions = "";               
                if(file_type == "pdf"){
                	allowed_extensions = new Array("pdf","PDF");	
                }else{
                	allowed_extensions = new Array("jpeg","png","JPEG","jpg","JPG","PNG");	
                }
               
                   var boolean = false; 
                   var g1 = 0;
//                   var allowed_extensions = new Array("csv","xls","xlsx","jpeg","png","jpg","gif","bmp","PNG","pdf");                   
//                   var allowed_extensions = new Array("jpeg","png","JPEG","jpg","JPG","PNG","pdf","PDF");
                   //var file_extension = e1.split('.').pop();
               
                    for(g1 = 0; g1 < allowed_extensions.length; g1++){
					  if (e1.substr(e1.length - allowed_extensions[g1].length, allowed_extensions[g1].length).toLowerCase() == allowed_extensions[g1].toLowerCase()) {
                      boolean = true;
                     }
                    }
                    if (!boolean) {
                    alert("Sorry,   " + e1 + "  is invalid, \n Allowed extensions are : " + allowed_extensions);
                    document.getElementById('file'+jm).value = "";                    
                    return false;
                   } else {
                    var FileSize = document.getElementById('file'+jm).files[0].size / 1024; // in KB
				if(file_type == "pdf"){
					var FileSize1 = FileSize / 1024; // in MB
				  if (FileSize1 > 1) {
                        alert('File size exceeds 1 MB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    }else if (FileSize <= 0) {
                        alert('File Size should not 0 KB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    }
                    }else{
                    if (FileSize > 200) {
                        alert('File size exceeds 200 KB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    } else if (FileSize <= 0) {
                        alert('File Size should not 0 KB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    }
                    }
                    
                                                                   
//==============================================  
                 }
				}
			     jsonObj1[label] = e1;
			    // jm++;
				}else{			    
				jsonObj1[label] = e1;
			    // jm++;
				}
  				jm++;
              } else if(dtype == "text"){
                b1 = document.getElementById('text_'+i).value;
				var y1 = 0, x4 = 0;
                if (b1 == "" || b1 == null)
                    {
                        alert("Please Enter "+label+" ");
                        return false;
                    }  
                    else if (!b1.match(Text_area))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else if (b1.length > max || b1.length < min) {
                        alert("Length Exceeded==" +b1);
                        return false;
                    } else {
                        for (y1 = 0; y1 < b1.length; y1++)
                        {
                            var c7 = b1.charAt(y1);
                            if ((c7 >= 'a') && (c7 <= 'z')) {
                                text = true;
                            }
                            if ((c7 >= 'A') && (c7 <= 'Z')) {
                                text1 = true;
                            }

                         }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+"  should be allow Characters,numbers and special character's only");
                            return false;
                        }
                    } 
                		jsonObj1[label] = b1;   
                }else if(dtype == "number"){
                        a26 = document.getElementById('number_'+i).value;
                         if (a26 == "" || a26 == null)
                            {
                                alert("Please Enter "+label+" ");
                                return false;
                            }  
                            else if (!a26.match(captcha_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                                return false;
                            } else if (a26.length > max || a26.length < min) {
                                alert("Length Exceeded==" +a26);
                                return false;
                            } 
                        		jsonObj1[label] = a26;
                 }else if(dtype == "mobile"){
                                a27 = document.getElementById('mobile_'+i).value;
                                if (a27 == "" || a27 == null)
                                    {
                                        alert("Please Enter "+label+" ");
                                        return false;
                                    }  
                                    else if (!a27.match(captcha_pattern))
                                    {
                                        alert("Enter  Valid "+label+" ");
                                        return false;
                                    } else if (a27.length > max || a27.length < min) {
                                        alert("Length Exceeded==" +a27);
                                        return false;
                                    }
                                		jsonObj1[label] = a27;
                }else	if(dtype == "aadhaarno"){
                        a28 = document.getElementById('aadhaarno_'+i).value;
                        if (a28 == "" || a28 == null)
                            {
                                alert("Please Enter "+label+" ");
                                return false;
                            }  
                            else if (!a28.match(captcha_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                                return false;
                            } else if (a28.length > max || a28.length < min) {
                                alert("Length Exceeded==" +a28);
                                return false;
                            }
                        		jsonObj1[label] = a28;
                }else	if(dtype == "panno"){
                        a29 = document.getElementById('panno_'+i).value;
                        if (a29 == "" || a29 == null)
                            {
                                alert("Please Enter "+label+" ");
                                return false;
                            }  
                            else if (!a29.match(pan_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                 
                                return false;
                            } else if (a29.length > max || a29.length < min) {
                                alert("Length Exceeded==" +a29);
                                return false;
                            }
                        		jsonObj1[label] = a29;
                        }else if(dtype == "time"){
                        a30 = document.getElementById('time_'+i).value;
                         if (a30 == "" || a30 == null)
                            {
                                alert("Please Enter "+label+" ");
                                return false;
                            }  
                            else if (!a30.match(time_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                                return false;
                            } else if (a30.length > max || a30.length < min) {
                                alert("Length Exceeded==" +a30);
                                return false;
                            } 
                        		jsonObj1[label] = a30;
                        }else if(dtype == "otp"){
                        a34 = document.getElementById('otp_'+i).value;
                         if (a34 == "" || a34 == null)
                            {
                                alert("Please Enter OTP ");
                                return false;
                            }  
                            else if (!a34.match(captcha_pattern))
                            {
                                alert("Enter  Valid OTP ");
                                return false;
                            } else if (a34.length > max || a34.length < min) {
                                alert("Length Exceeded==" + a34);
                                return false;
                            } 
                        		jsonObj1[label] = a34; 
                 }
//---------------------Mandatory--False------------------------------						
                }else {					
		        if (dtype == "dropdown") { 
                a1 = document.getElementById('dropdown_'+i).value; 
                var x1 = 0;
                    if (a1 != "" && a1 != null)
                    {
                        if (!a1.match(user_type_pattern))
                    {
                        alert("select  Valid "+label+" ");
                        return false;
                    } else if (a1.length > max || a1.length < min) {
                        alert("Length Exceeded==" +a1);
                        return false;
                    } else {
                        for (x1 = 0; x1 < a1.length; x1++)
                        {
                            var c1 = a1.charAt(x1);
                            if ((c1 >= 'a') && (c1 <= 'z')) {
                                text = true;
                            }
                            if ((c1 >= 'A') && (c1 <= 'Z')) {
                                text1 = true;
                            }

                        }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+" should be Enter Characters only");
                            return false;
                        }
                    } 
                    
                    }
                        jsonObj1[label] = a1;
                        
                }else	if (dtype == "specialcombo") { 
                	    a23 = document.getElementById('stateCode').value;
                        if (a23 != "" && a23 != null)
                        {
                             if (!a23.match(captcha_pattern))
                        {
                            alert("select  Valid State ");
                            return false;
                        } else if (a23.length > 5 || a23.length < 1) {
                            alert("Length Exceeded==" +a23);
                            return false;
                        } else {}
                        }
                            jsonObj1['stateCode'] = a23;
                		
                        a24 = document.getElementById('distCode').value; 
                            if (a24 != "" && a24 != null)
                            {
                                if (!a24.match(captcha_pattern))
                            {
                                alert("select  Valid District ");
                                return false;
                            } else if (a24.length > 5 || a24.length < 1) {
                                alert("Length Exceeded==" +a24);
                                return false;
                            } else {}
                            }
                                jsonObj1['distCode'] = a24;  
                      
                                a25 = document.getElementById('mandalCode').value; 
                                    if (a25 != "" && a25 != null)
                                    {
                                        if (!a25.match(captcha_pattern))
                                    {
                                        alert("select  Valid Mandal ");
                                        return false;
                                    } else if (a25.length > 10 || a25.length < 1) {
                                        alert("Length Exceeded==" +a25);
                                        return false;
                                    }else{}
                                    }
                                        jsonObj1['mandalCode'] = a25;       
                                
                                        a26 = document.getElementById('villCode').value; 
                                            if (a26 != "" && a26 != null)
                                            {
                                                 if (!a26.match(captcha_pattern))
                                            {
                                                alert("select  Valid Village ");
                                                return false;
                                            } else if (a26.length > 10 || a26.length < 1) {
                                                alert("Length Exceeded==" +a26);
                                                return false;
                                            }else{}
                                            }
                                                jsonObj1['villCode'] = a26;       
		    var villName = document.getElementById("villCode").options[document.getElementById("villCode").selectedIndex].text;
     		document.getElementById("villName").value = villName;
                                                                                                  
        }else   if (dtype == "radiobutton") {                  	
                var selchbox1 = "";
                var d = document.getElementsByName('radio_button_'+i);  
                var h= d.length;
                var x2 = 0,p1 = 0;;
                for(p1 = 0; p1<h; p1++) {
                    if(d[p1].type == 'radio' && d[p1].checked == true){ 
                    selchbox1 = d[p1].value;      
                   }      
                  }                                                                                             
                    if (selchbox1 != "" && selchbox1 != null)
                    {
                         if (!selchbox1.match(String))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else if (selchbox1.length > max || selchbox1.length < min) {
                        return false;
                    } else {
                        for (x2 = 0; x2 < selchbox1.length; x2++)
                        {
                            var c2 = selchbox1.charAt(x2);
                            if ((c2 >= 'a') && (c2 <= 'z')) {
                                text = true;
                            }
                            if ((c2 >= 'A') && (c2 <= 'Z')) {
                                text1 = true;
                            }

                        }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+" should be Enter Characters only");
                            return false;
                        }
                    }
                    }else{
					 selchbox1 = "";	
					}
                    
                        jsonObj1[label] = selchbox1;
        } else   if (dtype == "checkbox") {                  	   
                    var selchbox = [];
                    var c3 = document.getElementsByName('check_box_'+i);                     
                    var g = c3.length;
                    var j=0, t1 = 0, k1 = 0,u1 = 0;;
                    for(t1 = 0; t1<g; t1++) {
                    if(c3[t1].type == 'checkbox' && c3[t1].checked == true){ 
                    selchbox[j] = c3[t1].value; 
                    j++;  
                    }      
                    }
                    if(selchbox.length != "0"){
                       for(k1 = 0; k1<selchbox.length ;k1++){
                     if (!selchbox[k1].match(String))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else if (selchbox[k1].length > max || selchbox[k1].length < min) {
                        alert("Length Exceeded==" +selchbox[k1]);
                        return false;
                    } else {
                        for (u1 = 0; u1 < selchbox[k1].length; u1++)
                        {
                            var c4 = selchbox[k1].charAt(u1);
                            if ((c4 >= 'a') && (c4 <= 'z')) {
                                text = true;
                            }
                            if ((c4 >= 'A') && (c4 <= 'Z')) {
                                text1 = true;
                            }

                        }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+" should be Enter Characters only");
                            return false;
                        }
                    }
                }
                }
                    jsonObj1[label] = selchbox;                    
        }else   if (dtype == "textarea") {                    	
                    var e = document.getElementById('textarea_'+i).value; 
                    var x3 = 0;
                  if (e != "" && e != null)
                    {
                         if (!e.match(Text_area))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else {
                        if (e.length > max || e.length < min) {
                        alert("Length Exceeded==" +e);
                        return false;
                          }
                     }
                    }

                    jsonObj1[label] = e;
        } else  if (dtype == "password") {                	
                    var f = document.getElementById('password_'+i).value; 
                    var w1 = 0;var pwd = "";
                  if (f != "" && f != null)
                    {
                     if (f.length > max || f.length < min) {
                       alert("password should be minimum '"+min+"' and maximum '"+max+"' digit With Combination of Lower Case and Upper Case and Numbers,one Special Symbol(@,#,$,*,&)" +f);
                        return false;
                    } else {
                        for (w1 = 0; w1 < f.length; w1++)
                        {                           
                            var c6 = f.charAt(w1);                             
                            if ((c6 >= 'a') && (c6 <= 'z')) {
                                text = true;
                            }
                            if ((c6 >= 'A') && (c6 <= 'Z')) {
                                text1 = true;
                            }
                            if ((c6 >= '0') && (c6 <= '9')) {
                                numeric = true;
                            }
                            if ((c6 == '*') || (c6 == '#') || (c6 == '&') || (c6 == '@') || (c6 == '$')) {
                                specialCharsAll = true;
                            }
                        }                        
                        if (text == false || text1 == false || numeric == false || specialCharsAll == false)
                        {
                            alert(" "+label+"  should be Enter Combination of Lower Case and Upper Case and Number,one Special Symbol(@,#,$,*,&)");
                            return false;
                        }
                    }                    
                  pwd = SHA256(f);
                  }else{
					pwd = "";  
				  }                  
                  jsonObj1[label] = pwd;
        } else  if(dtype == "email"){                   	
                 c1 = document.getElementById('email_'+i).value;
                if (c1 != "" && c1 != null)
                    {
                         if (!c1.match(email2_val))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else {
                        if (c1.length > max || c1.length < min) {
                        alert("Length Exceeded==" +c1);
                        return false;
                    }
                   }
					}
                 jsonObj1[label] = c1;    
        } else  if(dtype == "date"){                         	
                	 
                 d1 = document.getElementById('date'+x).value;                  
                     
                if (d1 != "" && d1 != null)
                    {
                         if (!d1.match(date_pattern))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else {
                        if (d1.length > max || d1.length < min) {
                        alert("Length Exceeded==" +d1);
                        return false;
                    }
                   }
                   }
                 jsonObj1[label] = d1; 
                 x++;
        } else  if(dtype == "file"){
					var fileName = "";
//                	e1 = document.getElementById('file').files[j].value;
                 e1 = document.getElementById('file'+jm).value;                 
//               var  e1 = document.getElementsByName('file_'+i); 
                    if (e1 != "" && e1 != null )
                    {                  
               
//===================valid-code========================
                var allowed_extensions = "";				
                if(file_type == "pdf"){
                	allowed_extensions = new Array("pdf","PDF");	
                }else{
                	allowed_extensions = new Array("jpeg","png","JPEG","jpg","JPG","PNG");	
                }
                   var boolean = false; 
                   var g1 = 0;
                    for(g1 = 0; g1 < allowed_extensions.length; g1++)
                    {
						if (e1.substr(e1.length - allowed_extensions[g1].length, allowed_extensions[g1].length).toLowerCase() == allowed_extensions[g1].toLowerCase()) {
                                  boolean = true;
                                  break;
                        }
                    }
                    if (!boolean) {
                    alert("Sorry,   " + e1 + "  is invalid, \nAllowed extensions are : " + allowed_extensions);
                    document.getElementById('file'+jm).value = "";                    
                    return false;
                   } else {
                    var FileSize = document.getElementById('file'+jm).files[0].size / 1024; // in KB
				if(file_type == "pdf"){
					var FileSize1 = FileSize / 1024; // in MB
				  if (FileSize1 > 1) {
                        alert('File size exceeds 1 MB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    } else if (FileSize <= 0) {
                        alert('File Size should not 0 KB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    }
				}else{
                    if (FileSize > 200) {
                        alert('File size exceeds 200 KB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    } else if (FileSize <= 0) {
                        alert('File Size should not 0 KB');
                        document.getElementById('file'+jm).value = "";
                        return false;
                    }
                    }
                    }                                               
//==============================================   
                 jsonObj1[label] = e1;
			   //  jm++;
				}else{
				jsonObj1[label] = e1;
				 //jm++;
				}
				
				jm++;
             } else  if(dtype == "text"){
                b1 = document.getElementById('text_'+i).value;
				var y1 = 0, x4 = 0;
                if (b1 != "" && b1 != null)
                    {
                        if (!b1.match(Text_area))
                    {
                        alert("Enter  Valid "+label+" ");
                        return false;
                    } else if (b1.length > max || b1.length < min) {
                        alert("Length Exceeded==" +b1);
                        return false;
                    } else {
                        for (y1 = 0; y1 < b1.length; y1++)
                        {
                            var c7 = b1.charAt(y1);
                            if ((c7 >= 'a') && (c7 <= 'z')) {
                                text = true;
                            }
                            if ((c7 >= 'A') && (c7 <= 'Z')) {
                                text1 = true;
                            }

                         }
                        if (text == "false" || text1 == "false")
                        {
                            alert(" "+label+"  should be allow Characters,numbers and special character's only");
                            return false;
                        }
                    }
                    }
                		jsonObj1[label] = b1;
                }else if(dtype == "number"){
                        a26 = document.getElementById('number_'+i).value;
                        if (a26 != "" && a26 != null)
                            {
                                 if (!a26.match(captcha_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                                return false;
                            } else if (a26.length > max || a26.length < min) {
                                alert("Length Exceeded==" +a26);
                                return false;
                            }
                            }
                        		jsonObj1[label] = a26;
                 }else	if(dtype == "mobile"){
                                a27 = document.getElementById('mobile_'+i).value;
                                if (a27 != "" && a27 != null)
                                    {
                                         if (!a27.match(captcha_pattern))
                                    {
                                        alert("Enter  Valid "+label+" ");
                                        return false;
                                    } else if (a27.length > max || a27.length < min) {
                                        alert("Length Exceeded==" +a27);
                                        return false;
                                    }
                                    }
                                		jsonObj1[label] = a27;
                }else	if(dtype == "aadhaarno"){
                        a28 = document.getElementById('aadhaarno_'+i).value;
                        if (a28 != "" && a28 != null)
                            {
                               if (!a28.match(captcha_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                                return false;
                            } else if (a28.length > max || a28.length < min) {
                                alert("Length Exceeded==" +a28);
                                return false;
                            }
                            }
                        		jsonObj1[label] = a28;
                }else	if(dtype == "panno"){
                        a29 = document.getElementById('panno_'+i).value;
                        if (a29 != "" && a29 != null)
                            {
                                 if (!a29.match(pan_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                                return false;
                            } else if (a29.length > max || a29.length < min) {
                                alert("Length Exceeded==" +a29);
                                return false;
                            }
                            }
                        		jsonObj1[label] = a29;
                        }else	if(dtype == "time"){
                        a30 = document.getElementById('time_'+i).value;
                        if (a30 != "" && a30 != null)
                            {
                                 if (!a30.match(time_pattern))
                            {
                                alert("Enter  Valid "+label+" ");
                                return false;
                            } else if (a30.length > max || a30.length < min) {
                                alert("Length Exceeded==" +a30);
                                return false;
                            }
                            }
                        		jsonObj1[label] = a30;
                        }else	if(dtype == "otp"){
                        a34 = document.getElementById('otp_'+i).value;
                        if (a34 == "" || a34 == null)
                            {
                                alert("Please Enter OTP ");
                                return false;
                            }else if (!a34.match(time_pattern))
                            {
                                alert("Enter  Valid OTP ");
                                return false;
                            } else if (a34.length > max || a34.length < min) {
                                alert("Length Exceeded==" + a34);
                                return false;
                            }
                            
                        		jsonObj1[label] = a34;
                        }					                
                }                 
                    arr[i] = jsonObj1;                     
                }  
        	
        var captcha_key = document.getElementById('captcha_key').value;
        if(captcha_key == 'Y'){
		var captchaSecurity = document.getElementById('captcha_security').value;
		if(captchaSecurity === "" || captchaSecurity === null){
		 alert("Refresh Captcha.");
		 document.getElementById('captcha').value = "";
         return false;					
		}	
        var captcha = document.getElementById('captcha').value;
        if (captcha == "" || captcha == null)
            {
                alert("Please Enter Captcha ");
                return false;
            }  
            else if (!a28.match(pass_type_pattern))
            {
                alert("Enter Valid Captcha ");
                return false;
            }else{
		     var captcha_security = document.getElementById('captcha_security').value;
		     if(!captcha_security.match(captcha)){
			  	alert("Enter Correct Captcha ");
                return false;	 
			 }	
			}
            
            
  			}
        
                    var jsonString = JSON.stringify(arr);   
                    return jsonString;
                
  }
                        

  function getShow(no, num) {
      if (no == 'true') {                
       document.getElementById("sss_" + num).style.display = 'block';
       document.getElementById("ssr_" + num).style.display = 'block';
        
       var table = "";
       document.getElementById("rrr_" + num).style.display = 'none';
       document.getElementById("bbb_" + num).style.display = 'none';   
       document.getElementById("rr_" + num).style.display = 'none'; 
       document.getElementById('bb_'+num).style.display = 'none';
       
       document.getElementById("ccc_" + num).style.display = 'none';
       document.getElementById("cb_" + num).style.display = 'none';   
       document.getElementById("cc_" + num).style.display = 'none'; 
       document.getElementById('cbv_'+ num).style.display = 'none';
      } else {
       var table = "";
       document.getElementById("sss_" + num).style.display = 'none';
       document.getElementById("ssr_" + num).style.display = 'none';   
       document.getElementById("nnn_" + num).style.display = 'none'; 
       document.getElementById('vvv_'+num).innerHTML = table;
      }
  }
  
  function getShow1(no1, num1) {
        var table = "";
      for (var j = 0; j < no1; j++) {
          //table = table + " <input type='text' name='dropvalues' style='width:80px;' id='dropvalues_" +  num1 + "" +  j + "' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Drop Value');' autocomplete='off'>";                  
			table = table + " <div class='dropdown-item''><input type='text' class='form-control' placeholder='Custom Input' name='dropvalues'  id='dropvalues_" +  num1 + "" +  j + "' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Drop Value');' autocomplete='off'></div>";
      }                    
      document.getElementById('nnn_'+num1).style.display = 'block';
      document.getElementById('vvv_'+num1).innerHTML = table;
      document.getElementById('vvv_'+num1).style.display = 'block';
  }
  function getShow2(no2, num2) {
      if (no2 == 'true') {
          document.getElementById("rrr_" + num2).style.display = 'block';
          document.getElementById("bbb_" + num2).style.display = 'block';
          
       var table = "";
      } else {
          var table = "";
       document.getElementById('bb_'+ num2).innerHTML = table;
      }
  }
  function getShow3(no3, num3) {                
      var table = "";
      for (var k = 0; k < no3; k++) {
         // table = table + " <input type='text' name='radiovalues' style='width:80px;' id='radiovalues_" +  num3 + "" +  k + "' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Radio Value');' autocomplete='off'>";          
          table = table + " <div class='dropdown-item'><input type='text' class='form-control'  name='radiovalues'  placeholder='Custom Input' id='radiovalues_" +  num3 + "" +  k + "' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Radio Value');' autocomplete='off'></div>";
      }               
      document.getElementById('rr_'+num3).style.display = 'block';
      document.getElementById('bb_'+num3).innerHTML = table;
      document.getElementById('bb_'+num3).style.display = 'block';
  }
  function getShow4(no4, num4) {
      if (no4 == 'true') {
          document.getElementById("ccc_" + num4).style.display = 'block';
          document.getElementById("cb_" + num4).style.display = 'block';         

      } else {
      }
  }
  function getShow5(no5, num5) {
      var table = "";
      for (var m = 0; m < no5; m++) {
          //table = table + " <input type='text' name='checkvalues' style='width:80px;' id='checkvalues_" +  num5 + "" +  m + "' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Check Value');' autocomplete='off'>";
          table = table + " <div class='dropdown-item'><input type='text' name='checkvalues' id='checkvalues_" +  num5 + "" +  m + "' minlength ='1' maxlength='25' onkeypress='return ValidateAlpha(event)' onblur='return checkString(this, 'Check Value');' autocomplete='off'></div>";
      }               
      document.getElementById('cc_'+num5).style.display = 'block';
      document.getElementById('cbv_'+num5).innerHTML = table;
      document.getElementById('cbv_'+num5).style.display = 'block';
  }
  /*function getMandatory(no, num){
    var dtype = document.getElementById("dtype_" + num);
	if(dtype == 'otp') {
		if(no == true){
	   document.getElementById("sms").value='Y';
	  }else{
	   document.getElementById("sms").value='N';  
	  }	
	} 
  } */
  function getShowType(no, num) {	  	
	  		//var textField = document.getElementById("label_" + num);
            //textField.removeAttribute("readonly");
            
            var textField1 = document.getElementById("minsize_" + num);
            textField1.removeAttribute("readonly");
            
            var textField2 = document.getElementById("maxsize_" + num);
            textField2.removeAttribute("readonly");            		  	
						
			document.getElementById("minsize_" + num).value = '';
			document.getElementById("maxsize_" + num).value = '';
	  if (no == 'text' || no == 'textarea' || no == 'password' || no == 'email' || no == 'date'  || no == 'mobile' || no == 'panno' || no == 'aadhaarno' || no == 'number' || no == "specialcombo" || no == "time" ) {
      var table = "";
       document.getElementById("sss_" + num).style.display = 'none';
       document.getElementById("ssr_" + num).style.display = 'none';   
       document.getElementById("nnn_" + num).style.display = 'none'; 
       document.getElementById('vvv_' + num).style.display = 'none';
       document.getElementById('vvv_' + num).innerHTML = table;
       
       document.getElementById("rrr_" + num).style.display = 'none';
       document.getElementById("bbb_" + num).style.display = 'none';   
       document.getElementById("rr_" + num).style.display = 'none'; 
       document.getElementById('bb_' +num).style.display = 'none';
       document.getElementById('bb_'+num).innerHTML = table;
       
       document.getElementById("ccc_" + num).style.display = 'none';
       document.getElementById("cb_" + num).style.display = 'none';   
       document.getElementById("cc_" + num).style.display = 'none'; 
       document.getElementById('cbv_'+ num).style.display = 'none';
       document.getElementById('cbv_'+ num).innerHTML = table; 
       
      document.getElementById("ff2_" + num).style.display = 'none';
      document.getElementById("ff3_" + num).style.display = 'none';
        /*if(no == 'otp'){
			document.getElementById("label_" + num).value = 'OTP verify';
			document.getElementById("minsize_" + num).value = '6';			
			document.getElementById("maxsize_" + num).value = '6';
			document.getElementById("minsize_" + num).readOnly = 'true';
			document.getElementById("maxsize_" + num).readOnly = 'true';
			document.getElementById("label_" + num).readOnly = 'true';
			document.getElementById("sms").value = 'Y';
		}else */
		if(no == 'date'){
			document.getElementById("minsize_" + num).value = '10';
			document.getElementById("maxsize_" + num).value = '10';
			document.getElementById("minsize_" + num).readOnly = 'true';
			document.getElementById("maxsize_" + num).readOnly = 'true';
		}else if(no == 'mobile'){
			document.getElementById("minsize_" + num).value = '10';
			document.getElementById("maxsize_" + num).value = '10';
			document.getElementById("minsize_" + num).readOnly = 'true';
			document.getElementById("maxsize_" + num).readOnly = 'true';
		}else if(no == 'panno'){
			document.getElementById("minsize_" + num).value = '10';
			document.getElementById("maxsize_" + num).value = '10';		
			document.getElementById("minsize_" + num).readOnly = 'true';
			document.getElementById("maxsize_" + num).readOnly = 'true';
		}else if(no == 'aadhaarno'){
			document.getElementById("minsize_" + num).value = '12';
			document.getElementById("maxsize_" + num).value = '12';
			document.getElementById("minsize_" + num).readOnly = 'true';
			document.getElementById("maxsize_" + num).readOnly = 'true';
		}else if(no == 'time'){
			document.getElementById("minsize_" + num).value = '5';
			document.getElementById("maxsize_" + num).value = '5';
			document.getElementById("minsize_" + num).readOnly = 'true';
			document.getElementById("maxsize_" + num).readOnly = 'true';
		}
		
          
      } else if (no == 'dropdown') {
    //   document.getElementById("drop_" + num).style.display = 'block';
    //   document.getElementById("drop1_" + num).style.display = 'block';
       document.getElementById("sss_" + num).style.display = 'block';
       document.getElementById("ssr_" + num).style.display = 'block';
     //  document.getElementById("multicolumn_" + num).value = "true";
       

       var table = "";
       document.getElementById("rrr_" + num).style.display = 'none';
       document.getElementById("bbb_" + num).style.display = 'none';   
       document.getElementById("rr_" + num).style.display = 'none'; 
       document.getElementById('bb_'+num).style.display = 'none';
       document.getElementById('bb_'+ num).innerHTML = table;
       
       document.getElementById("ccc_" + num).style.display = 'none';
       document.getElementById("cb_" + num).style.display = 'none';   
       document.getElementById("cc_" + num).style.display = 'none'; 
       document.getElementById('cbv_'+ num).style.display = 'none';
       document.getElementById('cbv_'+ num).innerHTML = table;
       

      document.getElementById("ff2_" + num).style.display = 'none';
      document.getElementById("ff3_" + num).style.display = 'none';
          
      } else if (no == 'radiobutton') {
    //   document.getElementById("radio_" + num).style.display = 'block';
    //   document.getElementById("radio1_" + num).style.display = 'block';
       document.getElementById("rrr_" + num).style.display = 'block';
       document.getElementById("bbb_" + num).style.display = 'block'; 
      // document.getElementById("radiobutton_" + num).value = "true"; 
       

       var table = "";
       document.getElementById("sss_" + num).style.display = 'none';
       document.getElementById("ssr_" + num).style.display = 'none';   
       document.getElementById("nnn_" + num).style.display = 'none'; 
       document.getElementById("vvv_" + num).style.display = 'none'; 
       document.getElementById('vvv_'+ num).innerHTML = table;                                                
       
       document.getElementById("ccc_" + num).style.display = 'none';
       document.getElementById("cb_" + num).style.display = 'none';   
       document.getElementById("cc_" + num).style.display = 'none'; 
       document.getElementById("cbv_" + num).style.display = 'none';
       document.getElementById('cbv_'+ num).innerHTML = table;
   
      document.getElementById("ff2_" + num).style.display = 'none';
      document.getElementById("ff3_" + num).style.display = 'none';
          
      } else if (no == 'checkbox') {
    //   document.getElementById("check_" + num).style.display = 'block';
    //   document.getElementById("check1_" + num).style.display = 'block';
       document.getElementById("ccc_" + num).style.display = 'block';
       document.getElementById("cb_" + num).style.display = 'block';
     //  document.getElementById("checkbox_" + num).value = "true"; 
       
       
       var table = "";
       document.getElementById("sss_" + num).style.display = 'none';
       document.getElementById("ssr_" + num).style.display = 'none';   
       document.getElementById("nnn_" + num).style.display = 'none'; 
       document.getElementById('vvv_' + num).innerHTML = table;
       
       document.getElementById("rrr_" + num).style.display = 'none';
       document.getElementById("bbb_" + num).style.display = 'none';   
       document.getElementById("rr_" + num).style.display = 'none'; 
       document.getElementById('bb_'+num).innerHTML = table;
       
       
     
      document.getElementById("ff2_" + num).style.display = 'none';
      document.getElementById("ff3_" + num).style.display = 'none';
          
      } 
      else if (no == 'file') {
       document.getElementById("ff2_" + num).style.display = 'block';
       document.getElementById("ff3_" + num).style.display = 'block';
       
       
       var table = "";
       document.getElementById("sss_" + num).style.display = 'none';
       document.getElementById("ssr_" + num).style.display = 'none';   
       document.getElementById("nnn_" + num).style.display = 'none'; 
       document.getElementById('vvv_' + num).innerHTML = table;
       
       document.getElementById("rrr_" + num).style.display = 'none';
       document.getElementById("bbb_" + num).style.display = 'none';   
       document.getElementById("rr_" + num).style.display = 'none'; 
       document.getElementById('bb_'+num).innerHTML = table;
       
       document.getElementById("ccc_" + num).style.display = 'none';
       document.getElementById("cb_" + num).style.display = 'none';   
       document.getElementById("cc_" + num).style.display = 'none'; 
       document.getElementById('cbv_'+ num).innerHTML = table; 
             
      } else{
          
      }                                
  
  
}

  var _validFileExtensions = [".jpg", ".JPG", ".jpeg", ".JPEG", ".png", ".PNG",".pdf",".PDF"];
  var _validFileExtensions1 = [".pdf", ".PDF"];
  
  function ValidateSize(file) {
      if (file.type == "file") {
          var sFileName = file.value;
          if (sFileName.length > 0) {
              var blnValid = false;
              for (var j = 0; j < _validFileExtensions.length; j++) {
                  var sCurExtension = _validFileExtensions[j];
                  if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                      blnValid = true;
                      break;
                  }
              }

              if (!blnValid) {
                  alert("only JPG/JPEG/PNG files allowed");
                  file.value = "";
                  return false;
              }
              else {
//                  alert(file.files[0].size);
                  var FileSize = file.files[0].size / 1024; // in KB
//                  alert(FileSize + "KB");
//                  if (FileSize > 50) {
                  if (FileSize > 50) {
                      alert('File size exceeds 50 KB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  } else if (FileSize <= 0) {
                      alert('File Size should not 0 KB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  }
              }
          }
      }
  }
  function ValidateSize1(file) {
      if (file.type == "file") {
          var sFileName = file.value;
          if (sFileName.length > 0) {
              var blnValid = false;
              for (var j = 0; j < _validFileExtensions.length; j++) {
                  var sCurExtension = _validFileExtensions[j];
                  if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                      blnValid = true;
                      break;
                  }
              }

              if (!blnValid) {
                  alert("only JPG/JPEG/PNG/PDF files allowed");
                  file.value = "";
                  return false;
              }
              else {
//                  alert(file.files[0].size);
                  var FileSize = file.files[0].size / 1024; // in KB
//                  alert(FileSize + "KB");
//                  if (FileSize > 200) {
                  if (FileSize > 200) {

                      alert('File size exceeds 200 KB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  } else if (FileSize <= 0) {
                      alert('File Size should not 0 KB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  }
              }
          }
      }
  }
  function ValidateSize2(file) {
      if (file.type == "file") {
          var sFileName = file.value;
          if (sFileName.length > 0) {
              var blnValid = false;
              for (var j = 0; j < _validFileExtensions1.length; j++) {
                  var sCurExtension = _validFileExtensions1[j];
                  if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                      blnValid = true;
                      break;
                  }
              }

              if (!blnValid) {
                  alert("only pdf file allowed");
                  file.value = "";
                  return false;
              }
              else {
//                  alert(file.files[0].size);
                  var FileSize = file.files[0].size / 1024; // in KB
                  var FileSize10 = FileSize / 1024; // in MB

//                  alert(FileSize + "KB");
//                  if (FileSize1 > 1) {
                  if (FileSize10 > 1) {
                      alert('File size exceeds 1 MB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  } else if (FileSize <= 0) {
                      alert('File Size should not 0 KB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  }
              }
          }
      }
  }
  function ValidateSize3(file) {
      if (file.type == "file") {
          var sFileName = file.value;
          if (sFileName.length > 0) {
              var blnValid = false;
              for (var j = 0; j < _validFileExtensions.length; j++) {
                  var sCurExtension = _validFileExtensions[j];
                  if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                      blnValid = true;
                      break;
                  }
              }

              if (!blnValid) {
                  alert("only JPG/JPEG/PNG files allowed");
                  file.value = "";
                  return false;
              }
              else {
//                  alert(file.files[0].size);
                  var FileSize = file.files[0].size / 1024; // in KB
//                  alert(FileSize + "KB");
//                  if (FileSize > 200) {
                  if (FileSize > 5) {

                      alert('File size exceeds 5 KB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  } else if (FileSize <= 0) {
                      alert('File Size should not 0 KB');
                      //            $(file).val(''); //for clearing with Jquery
                      file.value = "";
                      return false;
                  }
              }
          }
      }
  }

  
	var base64String = "";
    function Uploaded(i,file) {
//----------------------------
        if (file.type == "file") {
            var sFileName = file.value;
            if (sFileName.length > 0) {
                var blnValid = false;
                for (var j = 0; j < _validFileExtensions.length; j++) {
                    var sCurExtension = _validFileExtensions[j];
                    if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                        blnValid = true;
                        break;
                    }
                }

                if (!blnValid) {
                    alert("only JPG/JPEG/PNG/PDF files allowed");
                    file.value = "";
                    return false;
                }
                else {
//                    alert(file.files[0].size);
                    var FileSize = file.files[0].size / 1024; // in KB
//                    alert(FileSize + "KB");
//                    if (FileSize > 200) {
                    if (FileSize > 200) {

                        alert('File size exceeds 200 KB');
                        //            $(file).val(''); //for clearing with Jquery
                        file.value = "";
                        return false;
                    } else if (FileSize <= 0) {
                        alert('File Size should not 0 KB');
                        //            $(file).val(''); //for clearing with Jquery
                        file.value = "";
                        return false;
                    }
                }
            }
        }
//----------------------------    	
    var file = document.querySelector('input[type=file]')['files'][0];
	//var file = document.querySelector('input[type=file]')['base64_'][i];
     var reader = new FileReader();
     reader.onload = function () {
        base64String = reader.result.replace("data:", "")
        .replace(/^.+,/, "");
        imageBase64Stringsep = base64String;
//        document.getElementById('base64_'+i).value = base64String;

     }
     reader.readAsDataURL(file);       
    }
    
//-------------Sha-256------------------------------------------
    function SHA256(s) {	
        var chrsz = 8;

        var hexcase = 0;


        function safe_add(x, y) {

            var lsw = (x & 0xFFFF) + (y & 0xFFFF);

            var msw = (x >> 16) + (y >> 16) + (lsw >> 16);

            return (msw << 16) | (lsw & 0xFFFF);

        }


        function S(X, n) {
            return (X >>> n) | (X << (32 - n));
        }

        function R(X, n) {
            return (X >>> n);
        }

        function Ch(x, y, z) {
            return ((x & y) ^ ((~x) & z));
        }

        function Maj(x, y, z) {
            return ((x & y) ^ (x & z) ^ (y & z));
        }

        function Sigma0256(x) {
            return (S(x, 2) ^ S(x, 13) ^ S(x, 22));
        }

        function Sigma1256(x) {
            return (S(x, 6) ^ S(x, 11) ^ S(x, 25));
        }

        function Gamma0256(x) {
            return (S(x, 7) ^ S(x, 18) ^ R(x, 3));
        }

        function Gamma1256(x) {
            return (S(x, 17) ^ S(x, 19) ^ R(x, 10));
        }


        function core_sha256(m, l) {

            var K = new Array(0x428A2F98, 0x71374491, 0xB5C0FBCF, 0xE9B5DBA5, 0x3956C25B, 0x59F111F1, 0x923F82A4, 0xAB1C5ED5, 0xD807AA98, 0x12835B01, 0x243185BE, 0x550C7DC3, 0x72BE5D74, 0x80DEB1FE, 0x9BDC06A7, 0xC19BF174, 0xE49B69C1, 0xEFBE4786, 0xFC19DC6, 0x240CA1CC, 0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA, 0x983E5152, 0xA831C66D, 0xB00327C8, 0xBF597FC7, 0xC6E00BF3, 0xD5A79147, 0x6CA6351, 0x14292967, 0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13, 0x650A7354, 0x766A0ABB, 0x81C2C92E, 0x92722C85, 0xA2BFE8A1, 0xA81A664B, 0xC24B8B70, 0xC76C51A3, 0xD192E819, 0xD6990624, 0xF40E3585, 0x106AA070, 0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5, 0x391C0CB3, 0x4ED8AA4A, 0x5B9CCA4F, 0x682E6FF3, 0x748F82EE, 0x78A5636F, 0x84C87814, 0x8CC70208, 0x90BEFFFA, 0xA4506CEB, 0xBEF9A3F7, 0xC67178F2);

            var HASH = new Array(0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A, 0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19);

            var W = new Array(64);

            var a, b, c, d, e, f, g, h, i, j;

            var T1, T2;


            m[l >> 5] |= 0x80 << (24 - l % 32);

            m[((l + 64 >> 9) << 4) + 15] = l;


            for (var i = 0; i < m.length; i += 16) {

                a = HASH[0];

                b = HASH[1];

                c = HASH[2];

                d = HASH[3];

                e = HASH[4];

                f = HASH[5];

                g = HASH[6];

                h = HASH[7];


                for (var j = 0; j < 64; j++) {

                    if (j < 16)
                        W[j] = m[j + i];

                    else
                        W[j] = safe_add(safe_add(safe_add(Gamma1256(W[j - 2]), W[j - 7]), Gamma0256(W[j - 15])), W[j - 16]);


                    T1 = safe_add(safe_add(safe_add(safe_add(h, Sigma1256(e)), Ch(e, f, g)), K[j]), W[j]);

                    T2 = safe_add(Sigma0256(a), Maj(a, b, c));


                    h = g;

                    g = f;

                    f = e;

                    e = safe_add(d, T1);

                    d = c;

                    c = b;

                    b = a;

                    a = safe_add(T1, T2);

                }


                HASH[0] = safe_add(a, HASH[0]);

                HASH[1] = safe_add(b, HASH[1]);

                HASH[2] = safe_add(c, HASH[2]);

                HASH[3] = safe_add(d, HASH[3]);

                HASH[4] = safe_add(e, HASH[4]);

                HASH[5] = safe_add(f, HASH[5]);

                HASH[6] = safe_add(g, HASH[6]);

                HASH[7] = safe_add(h, HASH[7]);

            }

            return HASH;

        }


        function str2binb(str) {

            var bin = Array();

            var mask = (1 << chrsz) - 1;

            for (var i = 0; i < str.length * chrsz; i += chrsz) {

                bin[i >> 5] |= (str.charCodeAt(i / chrsz) & mask) << (24 - i % 32);

            }

            return bin;

        }


        function Utf8Encode(string) {

            string = string.replace(/\r\n/g, "\n");

            var utftext = "";


            for (var n = 0; n < string.length; n++) {


                var c = string.charCodeAt(n);


                if (c < 128) {

                    utftext += String.fromCharCode(c);

                } else if ((c > 127) && (c < 2048)) {

                    utftext += String.fromCharCode((c >> 6) | 192);

                    utftext += String.fromCharCode((c & 63) | 128);

                } else {

                    utftext += String.fromCharCode((c >> 12) | 224);

                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);

                    utftext += String.fromCharCode((c & 63) | 128);

                }


            }


            return utftext;

        }


        function binb2hex(binarray) {

            var hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";

            var str = "";

            for (var i = 0; i < binarray.length * 4; i++) {

                str += hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4)) & 0xF) +
                        hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8)) & 0xF);

            }

            return str;

        }


        s = Utf8Encode(s);

        return binb2hex(core_sha256(str2binb(s), s.length * chrsz));

    }
//    -------End Sha-256---------------------------------------
    function isNumber(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {          
            return false;
        }
        return true;
    }
    
    function ValidateAlpha(evt) {
        evt = (evt) ? evt : window.event;  
         var keyCode = (evt.which) ? evt.which : evt.keyCode;
         if ((keyCode < 65 || keyCode > 90) && (keyCode < 97 || keyCode > 123) && keyCode != 32){ 
                 
         return false;
         }
       return true;
     }
     
     function ValidateAlphaNumeric(evt) {
     evt = (evt) ? evt : window.event;  
     var keyCode = (evt.which) ? evt.which : evt.keyCode;
     if ((keyCode < 65 || keyCode > 90) && (keyCode < 97 || keyCode > 123) && keyCode != 32 && (keyCode < 48 || keyCode > 57) && (keyCode != 46 && keyCode > 31)){         
     return false;
     }
      return true;
   }
   
   function isNumberwithColan(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;       
        if (charCode > 31 && (charCode < 48 || charCode > 58)) {          
            return false;
        }
        return true;
    }  
    
    function checkString(obj, mystring) {
        var ck_name = /^[a-zA-Z\s\. ]{0,100}$/;
        if (!ck_name.test(obj.value.trim())) {
            alert("only alphabet value allowed in " + mystring);
            obj.value = "";
            obj.focus();
            return false;
        }
    }
    
    function checkmobile(obj) {
    	  if (!/^[0]?[6789]\d{9}$/.test(obj.value)) {
    	        alert("Not a Valid Mobile no");
    	        obj.value = "";
    	        return false;
    	    } else if (obj.value=='9999999999' || obj.value=='8888888888' || obj.value=='7777777777' || obj.value=='6666666666' || obj.value=='5555555555' || obj.value=='4444444444' || obj.value=='3333333333' || obj.value=='2222222222' || obj.value=='1111111111' || obj.value=='0000000000') {
    	        alert("Not a Valid Mobile no");
    	        obj.value = "";// obj.value.substr(0, obj.value.length - 1);
    	        return false;
    	    } 
    	    else {
    	        obj.value = obj.value.toUpperCase();
    	        return true;
    	    }  
    	}
    
    function  aadharValid(obj) {
    	if (obj.value === '') {
            alert("Enter aadhaarno");
            obj.value="";
            return false;
        } else {
            aadharNew(obj);
        }
    }
    function aadharNew(obj) {

        var aadhar = obj.value;
        var validAid = false;
        if (aadhar === '') {
            return true;
        }
        if (aadhar ==='999999999999' || aadhar ==='888888888888' || aadhar ==='777777777777' || aadhar ==='666666666666' || aadhar ==='555555555555' || aadhar ==='444444444444' || aadhar ==='333333333333' || aadhar ==='222222222222' || aadhar ==='111111111111' || aadhar ==='000000000000') {
            alert("Not a valid Aadhaar number..");
            validAid = false;
        }

        if (validAid === true) {
            alert("Not a valid Aadhaar Number..");
            return false;
        }

        if (aadhar === '') {
        } else {
            if (aadhar.match(/^[A-Z]*$/)) {
                alert("Not a valid Aadhaar number..");
                //document.getElementById('representer_aadharno').value = "";
                obj.value = "";
                return false;
            }
            if (aadhar.match(/^(999999999999)$/)) {
                alert("Not a valid Aadhaar number..");
                //document.getElementById('representer_aadharno').value = "";
                obj.value = "";
                return false;
            }
            if (aadhar.match(/^[a-z]*$/)) {
                alert("Not a valid Aadhaar number..");
                //document.getElementById('representer_aadharno').value = "";
                obj.value = "";
                return false;
            }
            if (aadhar.verhoeffCheck()) {
            } else {
                alert("Not a valid Aadhaar number..");
                //document.getElementById('representer_aadharno').value = "";
                obj.value = "";
                return false;
            }
        }

        return true;
    }
    
    function textarea(obj, filedvalue) {
        //var splCharString = '<>%@~`!#$%^&*(){}[]|\/:;,+.=';
        var splCharString = "?^%@~`!#$%*:+\"\\'";
        var passval = obj.value;
        for (var i = 0; i < splCharString.length; i++)
        {
            if (parseInt(passval.indexOf(splCharString.charAt(i))) > 0) {
                alert(splCharString.charAt(i) + " symbole not allowed in " + filedvalue);
                obj.value = "";
                obj.focus();
                return false;
            }
        }
        return true;
    }
    
    function ValidateEmail(obj)
    {
    //var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    var mailformat =/^([a-zA-Z])([\w-.]*)@([\w]+)([\w-.])*\.(aero|asia|be|biz|com.ar|ca|co|co.in|co.jp|co.kr|co.sg|com|com.ar|com.mx|com.sg|com.ph|co.uk|coop|de|edu|es|fr|gov|in|info|it|jobs|ltd|mil|mobi|museum|name|net|net.mx|org|ru|us)+$/;
    if(!obj.value.match(mailformat)){
    //alert("You have entered an invalid email address!");
    obj.value = "";
    //obj.focus();
    return false;
    }
    }
    
    function validate() {
    if (document.getElementById('form_id').value == '') {
        alert('Please Select Form');
        document.getElementById('form_id').focus();
        return false;
    }
    } 
    function datesValid(){
	var currentDate = document.getElementById('currentDate').value;
	var to_date = document.getElementById('to_date').value;	
	var from_date = document.getElementById('from_date').value;
	let date1 = new Date(currentDate).getTime();
    let date2 = new Date(to_date).getTime();
    let date3 = new Date(from_date).getTime();
    
    if (date2 < date3) {
    alert("Please select Expiry To Date after Expiry From Date");
    return false;    
   } else if (date2 > date3) {    
  	} else {
    alert("Expiry From and Expiry To dates are equal");
    return false;
  }
    
	if (date1 < date2) {    
   } else if (date1 > date2) {
    alert("Please select To Date after Current Date")
    return false;
  } else {
    alert("Current Date and Expiry To dates are equal");
    return false;
  }     
	}
	
	function CheckAadhar(obj) {
    var aadhar = obj.value;
    var validAid = false;
    //var validEid = false;
    if (aadhar == '') {
        validAid = true;
    }
    if (aadhar == '999999999999') {
        alert("Not a valid Aadhar number..");
        validAid = false;
    }
    if (validAid == true) {
        //alert("Not a valid Aadhar Number..");
        return false;
    }
    if (aadhar == '') {
    } else {
        if (aadhar.match(/^[A-Z]*$/)) {
            alert("Not a valid Aadhar number..");
            obj.value = "";
            return false;
        }
        if (aadhar.match(/^(999999999999)$/)) {
            alert("Not a valid Aadhar number..");
            obj.value = "";
            return false;
        }
        if (aadhar.match(/^[a-z]*$/)) {
            alert("Not a valid Aadhar number..");
            obj.value = "";
            return false;
        }
        if (aadhar.verhoeffCheck()) {
        } else {
            alert("Not a valid Aadhar number..");
            obj.value = "";
            return false;
        }
    }
    return true;
}


String.prototype.verhoeffCheck = (function ()
{
    var d = [[0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
        [1, 2, 3, 4, 0, 6, 7, 8, 9, 5],
        [2, 3, 4, 0, 1, 7, 8, 9, 5, 6],
        [3, 4, 0, 1, 2, 8, 9, 5, 6, 7],
        [4, 0, 1, 2, 3, 9, 5, 6, 7, 8],
        [5, 9, 8, 7, 6, 0, 4, 3, 2, 1],
        [6, 5, 9, 8, 7, 1, 0, 4, 3, 2],
        [7, 6, 5, 9, 8, 2, 1, 0, 4, 3],
        [8, 7, 6, 5, 9, 3, 2, 1, 0, 4],
        [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]];
    var p = [[0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
        [1, 5, 7, 6, 2, 8, 3, 0, 9, 4],
        [5, 8, 0, 3, 7, 9, 6, 1, 4, 2],
        [8, 9, 1, 6, 0, 4, 3, 5, 2, 7],
        [9, 4, 5, 3, 1, 2, 6, 8, 7, 0],
        [4, 2, 8, 6, 5, 7, 3, 9, 0, 1],
        [2, 7, 9, 3, 8, 0, 6, 4, 1, 5],
        [7, 0, 4, 6, 9, 1, 3, 2, 5, 8]];
    var j = [0, 4, 3, 2, 1, 5, 6, 7, 8, 9];

    return function ()
    {
        var c = 0;
        this.replace(/\D+/g, "").split("").reverse().join("").replace(/[\d]/g, function (u, i, o) {
            c = d[c][p[i & 7][parseInt(u, 10)]];
        });
        return (c === 0);
    };
})();	


    function ValidateTime(obj)
    {    
    var timeformat =/^([0-9]{2})(:)([0-9]{2})+$/;
    if(!obj.value.match(timeformat)){
    obj.value = "";
    return false;
    }
    }

/*
-------------Checking-File-Content---------------
    var mimes = [
        {
            mime: 'image/jpeg',
            pattern: [0xFF, 0xD8, 0xFF],
            mask: [0xFF, 0xFF, 0xFF],
            extension: 'jpeg'
        },
        {
            mime: 'image/jpg',
            pattern: [0xFF, 0xD8, 0xFF, 0xE0],
            mask: [0xFF, 0xFF, 0xFF, 0xFF],
            extension: 'jpg'
        },
        {
            mime: 'image/png',
            pattern: [0x89, 0x50, 0x4E, 0x47],
            mask: [0xFF, 0xFF, 0xFF, 0xFF],
            extension: 'png'
        },
        {
        mime: 'application/pdf',
        pattern: [0x25, 0x50, 0x44, 0x46, 0x2d, 0x31, 0x2e],
        mask: [0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF],
        extension: 'pdf'
    	}
        // you can expand this list @see https://mimesniff.spec.whatwg.org/#matching-an-image-type-pattern
    ];
*/