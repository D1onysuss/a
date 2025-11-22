from flask import Blueprint, request, jsonify, redirect, url_for
from flask_login import login_user, logout_user
from extensions import db
from models import User

auth_bp = Blueprint("auth", __name__)

@auth_bp.post('/register')
def register():
    data = request.form
    # 检查邮箱是否已存在
    if User.query.filter_by(email=data['email']).first():
        return "Email already registered", 400
        
    # 创建新用户
    user = User(
        name=data['name'],
        email=data['email']
    )
    user.set_password(data['password'])
    
    db.session.add(user)
    db.session.commit()
    
    return redirect(url_for('web.login'))

@auth_bp.post('/login')
def login():
    data = request.form
    user = User.query.filter_by(email=data['email']).first()
    
    # 验证用户和密码
    if not user or not user.check_password(data['password']):
        return "Invalid email or password", 401
        
    login_user(user)  # 登录用户
    return redirect(url_for('web.home'))

@auth_bp.get('/logout')
def logout():
    logout_user()
    return redirect(url_for('web.home'))
