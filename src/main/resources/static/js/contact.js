$(document).ready(function(){
    
    (function($) {
        "use strict";

    
    jQuery.validator.addMethod('answercheck', function (value, element) {
        return this.optional(element) || /^\bcat\b$/.test(value)
    }, "type the correct answer -_-");

    // validate contactForm form
    $(function() {
        $('#contactForm').validate({
            rules: {
                name: {
                    required: true,
                    minlength: 2
                },
                subject: {
                    required: true,
                    minlength: 4
                },
                email: {
                    required: true,
                    email: true
                },
                message: {
                    required: true,
                    minlength: 20
                }
            },
            messages: {
                name: {
                    required: "хайде, имаш име, нали?",
                    minlength: "вашето име трябва да се състои от поне 2 знака."
                },
                subject: {
                    required: "хайде, имаш тема, нали?",
                    minlength: "вашата тема трябва да се състои от поне 4 знака."
                },
                email: {
                    required: "няма имейл, няма съобщение.",
                    email: "имейлът трябва да е валиден."
                },
                message: {
                    required: "хм... да, трябва да напишете нещо, за да изпратите този формуляр.",
                    minlength: "това е всичко? наистина ли?"
                }
            },
            submitHandler: function(form) {
                $(form).ajaxSubmit({
                    type:"POST",
                    url:"/Email/contact",
                    success: function() {
                        form.submit();
                    },
                    error: function() {
                        $('#contactForm').fadeTo( "slow", 1, function() {
                            $('#error').fadeIn()
                            $('.modal').modal('hide');
		                	$('#error').modal('show');
                        })
                    }
                })
            }
        })
    })
        
 })(jQuery)
})