def provision() {
    dir('foreman-infra') {
        git url: 'https://github.com/theforeman/foreman-infra.git'
    }

  	dir('foreman-infra/ci/centos.org/ansible') {
        runPlaybook('provision.yml', 'localhost')
    }
}

def deprovision() {
    if (fileExists('foreman-infra')) {
        dir('foreman-infra/ci/centos.org/ansible') {
            runPlaybook('deprovision.yml', 'localhost')
      	}
    }
}

def cico_inventory(relative_dir = '') {
    return relative_dir + 'foreman-infra/ci/centos.org/ansible/cico_inventory'
}

def ssh_config(relative_dir = '') {
    return relative_dir + 'foreman-infra/ci/centos.org/ansible/ssh_config'
}

def color_shell(command = '') {
    wrap([$class: 'AnsiColorBuildWrapper', colorMapName: "xterm"]) {
        sh "${command}"
    }
}

def duffy_ssh(command, box_name, relative_dir = '') {
    color_shell "ssh -F ${ssh_config(relative_dir)} ${box_name} '${command}'"
}

def duffy_scp(file_path, file_dest, box_name, relative_dir = '') {
    color_shell "scp -r -F ${ssh_config(relative_dir)} ${box_name}:${file_path} ${file_dest}"
}
