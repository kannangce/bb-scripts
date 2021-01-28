# Wrapper for frequency
function frequency() 
{
 bb -i -f $BB_SCRIPTS/src/frequency.clj -- $@
}

# mkdir and cd
function mkcd() { mkdir $1; cd $1 ;}

# git push for new branch
function push() { echo "Pushing to $(git branch --show-current)"; git push --set-upstream origin $(git branch --show-current);}

## Composed with clipboard
# format json
alias jlint='pbpaste|jq . |pbcopy'

# deduplicate
alias dedup='pbpaste| sort -u|pbcopy'
