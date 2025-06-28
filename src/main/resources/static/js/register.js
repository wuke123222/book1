            // 发送注册请求
            $.ajax({
                url: '/users/register',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    username: username,
                    email: email,
                    password: password,
                    nickname: nickname,
                    phone: phone
                }),
                success: function(response) {
                    alert('注册成功！请登录');
                    window.location.href = 'login.html';
                }
            }); // 修复：添加闭合的右括号以正确结束AJAX请求配置对象
            // 原错误：缺少闭合的}导致语法错误
