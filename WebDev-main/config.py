import os
basedir = os.path.abspath(os.path.dirname(__file__))


class DevConfig:
    SECRET_KEY = os.environ.get("SECRET_KEY", "dev-secret-key-change-me")
    SQLALCHEMY_DATABASE_URI = os.environ.get(
        "DATABASE_URL",
        "sqlite:///" + os.path.join(basedir, "the_green_kitchen.db")
    )
    SQLALCHEMY_TRACK_MODIFICATIONS = False
