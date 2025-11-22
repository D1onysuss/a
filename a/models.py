from datetime import datetime
from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin
from sqlalchemy import CheckConstraint
from extensions import db


class User(UserMixin, db.Model):
    __tablename__ = "users"

    id = db.Column(db.Integer, primary_key=True, autoincrement=True)  # 自增主键
    name = db.Column(db.String(100), nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)  # 邮箱唯一且必填
    password_hash = db.Column(db.String(256), nullable=False)  # 加密密码
    role = db.Column(db.String, default="reader")  # 生日可选（允许为空）
    created_at = db.Column(db.DateTime, default=datetime.utcnow)  # 注册时间（自动填充）

    # 密码加密方法
    def set_password(self, password):
        if not password:  # 防止空密码
            raise ValueError("Password cannot be empty")
        self.password_hash = generate_password_hash(password, method='pbkdf2:sha256')  # 强加密方式

    # 密码验证方法（后续登录用）
    def check_password(self, password):
        return check_password_hash(self.password_hash, password)
