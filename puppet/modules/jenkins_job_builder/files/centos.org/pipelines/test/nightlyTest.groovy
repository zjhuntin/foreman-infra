pipeline {
    agent { label 'foreman' }

    stages {
        stage('Setup Environment') {
            steps {

                deleteDir()

                git url: 'https://github.com/theforeman/forklift.git'

            }
        }

        stage('Provision Node') {
            steps {
                provision()
                sh "ls ${ssh_config('./')}"
            }
        }
        stage('Copy Forklift') {
            steps {
                runPlaybook('containers/tools/install-forklift.yml', cico_inventory('./'), ['@containers/vars/remote.yml'], ['-b'])

            }
        }
        stage('Install Pipeline Requirements') {
            steps {

                sh 'curl -L https://gist.github.com/zjhuntin/751f4c7ef49d92e0356e5454d5915b89/raw > playbooks/provision_test_box.yml'
                runPlaybook('playbooks/provision_test_box.yml', cico_inventory('./'), ['@containers/vars/remote.yml'], ['-b'])

            }
        }
        stage('Run Pipeline') {
            steps {
                sh "ssh duffy_box -F ${ssh_config('./')} 'cd forklift && systemctl restart libvirtd && vagrant up centos7'"
            }
        }
    }

      post {
            always {

                deprovision()
                deleteDir()

            }
      }
}
