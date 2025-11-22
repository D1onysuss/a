from flask import Flask
from config import DevConfig
from extensions import db, login_manager
from models import User


def create_app():
    app = Flask(__name__)
    app.config.from_object(DevConfig)

    # init extensions
    db.init_app(app)
    login_manager.init_app(app)

    # user loader for Flask-Login
    @login_manager.user_loader
    def load_user(user_id):
        return User.query.get(int(user_id))

    
    # register blueprints
    from blueprints.web.routes import web_bp
    from blueprints.api.auth import auth_bp

    app.register_blueprint(web_bp)
    app.register_blueprint(auth_bp, url_prefix="/api/auth")

    # create tables if not exists
    with app.app_context():
        db.create_all()

    return app


app = create_app()


if __name__ == "__main__":
    app.run(debug=True)
