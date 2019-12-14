int arrayValIn(int a){
   int qwer[2] = {100,200};
   qwer[a] = 300;
   return qwer[a];
}

void main() {
   _print(arrayValIn(1));
}