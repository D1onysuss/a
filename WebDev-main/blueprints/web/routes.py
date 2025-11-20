from flask import Blueprint, render_template
from flask_login import login_required, current_user

web_bp = Blueprint("web", __name__)


@web_bp.get("/")
def home():
    return render_template("home.html")


@web_bp.get("/login")
def login():
    return render_template("login.html")


@web_bp.get("/register")
def register():
    return render_template("register.html")


@web_bp.get("/profile")
@login_required
def profile():
    return render_template("profile.html", user=current_user)
