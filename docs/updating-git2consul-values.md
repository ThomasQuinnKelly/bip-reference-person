#How to Update git2consul Values

1) Find the appropriate yml file for your environment in `bip-participant-intake/ansible/environments` and add the desired git2consul properties. Default git2consul values that can be overwritten can be found in: `bip-participant-intake/ansible/roles/git2consul/defaults/main.yml`.

    * Example using Blue-Dev: If I wanted to update the branch git2consul is polling to development, I would add `consul_branch: development` to `bip-participant-intake/ansible/environments/blue-dev/group_vars/all/blue.yml`.
    
2) Run the git2consul.yml playbook using this command (replace `<project-name>` and `<environment>` with your desired values):

    * `ansible-playbook -i environments/<project-name>-<environment> git2consul.yml --ask-vault-pass`

3) In order for the git2consul changes to be applied, you will need to restart the git2consul deployment. That can be done by running this command (replace <namespace> and <git2consul deployment> with your desired values):
    * `kubectl rollout restart --namespace=<namespace> deployment/<git2consul deployment>`

4) Login to consul and confirm updated values.
   * Dev: https://consul.dev8.bip.va.gov/
   * Stage: https://consul.stage8.bip.va.gov/
   * Click on Key/Value at the top. Go into the config directory. There should be a git2consul config file and a directory (named according to your environment) containing the files polled from your consul_branch.
        * The git2consul config file should reflect the updates made.
        * If the `consul_branch` was updated, check the directory for your environment. If you changed the branch to development for example, you should now see a `development.ref` file and a new set of `.yml` files pulled from the development branch.
            * Note: The `.ref` and `.yml` files from the previous branch will still remain, but will no longer be updated since that branch is no longer being polled. These files can be deleted in Consul if no longer necessary.
