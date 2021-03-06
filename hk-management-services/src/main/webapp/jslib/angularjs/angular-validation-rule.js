(function() {
  angular
    .module('validation.rule', ['validation'])
    .config(['$validationProvider', function($validationProvider) {
      var expression = {
        required: function(value) {
          return !!value;
        },
        url: /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/,
        email: /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/,
        number: /^\d+$/,
        tel:/^1(3|4|5|6|7|8)\d{9}$/,
        minlength: function(value, scope, element, attrs, param) {
          return value && value.length >= param;
        },
        maxlength: function(value, scope, element, attrs, param) {
          return !value || value.length <= param;
        }
      };

      var defaultMsg = {
        required: {
          error: '此元素不能为空!',
          success: ''
        },
        url: {
          error: '请输入正确的URL',
          success: ''
        },
        email: {
          error: 'This should be Email',
          success: 'It\'s Email'
        },
        number: {
          error: '此元素必须为数字!',
          success: ''
        },
        minlength: {
          error: 'This should be longer',
          success: 'Long enough!'
        },
        maxlength: {
          error: 'This should be shorter',
          success: 'Short enough!'
        }
      };
      $validationProvider.setExpression(expression).setDefaultMsg(defaultMsg);
    }]);
}).call(this);
