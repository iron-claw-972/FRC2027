{
  pkgs,
  lib,
  config,
  inputs,
  ...
}:

{
  # https://devenv.sh/basics/
  env.GREET = "devenv";

  # https://devenv.sh/packages/
  packages = with pkgs; [
    git
    jujutsu
    google-java-format
  ];

  # https://devenv.sh/processes/
  # processes.dev.exec = "${lib.getExe pkgs.watchexec} -n -- ls -la";

  # https://devenv.sh/services/
  # services.postgres.enable = true;

  # https://devenv.sh/basics/
  enterShell = ''
    git --version
  '';

  # https://devenv.sh/tasks/
  # tasks = {
  #   "myproj:setup".exec = "mytool build";
  #   "devenv:enterShell".after = [ "myproj:setup" ];
  # };

  # https://devenv.sh/tests/
  enterTest = ''
    echo "Running tests"
    git --version | grep --color=auto "${pkgs.git.version}"
  '';
  # https://devenv.sh/languages/
  languages = {
    java = {
      enable = true;
      gradle = {
        enable = true;
      };
      jdk = {
        package = pkgs.openjdk21;
      };
    };
  };

  scripts = {
    build = {
      exec = "./gradlew build";
    };
    deploy = {
      exec = "./gradlew deployJava";
    };
    test = {
      exec = "./gradlew test";
    };
    clean = {
      exec = "./gradlew clean";
    };
  };

  # for taran's computer, should not run for anyone else
  profiles.user.moo.module = {
    env.JAVA_HOME = lib.mkForce "/usr/lib/jvm/java-21-openjdk";
  };

  # https://devenv.sh/git-hooks/
  # git-hooks.hooks.shellcheck.enable = true;

  git-hooks.hooks.formatting = {
    enable = true;
    name = "Formatting";
    entry = "google-java-format";
    # types = ["java"];
    files = "\\.(c|h)$";
  };

  # See full reference at https://devenv.sh/reference/options/
}
