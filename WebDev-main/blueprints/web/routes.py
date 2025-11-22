from flask import Blueprint, render_template
from flask_login import login_required, current_user
from flask import Blueprint, render_template, request  # 补充导入request
from extensions import db  # 导入数据库实例
from models import User  # 导入User模型

web_bp = Blueprint("web", __name__)


@web_bp.get("/")
def home():
    return render_template("home.html")


@web_bp.get("/login")
def login():
    return render_template("login.html")


@web_bp.get("/profile")
@login_required
def profile():
    return render_template("profile.html", user=current_user)


@web_bp.route("/register", methods=["GET", "POST"])
def register():
    if request.method == "POST":
        # 4. 获取表单提交的数据
        name = request.form.get("name")
        email = request.form.get("email")
        password = request.form.get("password")
        dob = request.form.get("dob")

        # 5. 检查邮箱是否已存在
        existing_user = User.query.filter_by(email=email).first()
        if existing_user:
            # 邮箱已存在：返回错误信息，仅保留姓名和生日（清除邮箱和密码）
            return render_template(
                "register.html",
                error="This email has already been registered",
                name=name,  # 保留姓名
                dob=dob     # 保留生日
                # 不返回email和password，实现自动清除
            )

        # 6. 邮箱不存在：创建新用户并保存到数据库
        new_user = User(email=email)
        new_user.set_password(password)  # 加密存储密码
        db.session.add(new_user)
        db.session.commit()

        # 7. 注册成功后跳转到登录页
        return render_template("login.html")  # 或使用redirect(url_for('web.login'))

    # GET请求：显示空的注册页面
    return render_template("register.html")
