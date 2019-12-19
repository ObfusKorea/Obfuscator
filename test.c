int arrayValIn(int index){
   int qwer[3] = {99,98};
qwer[index] = 1;
   return qwer[index];
}

void main() {
   _printI(arrayValIn(2));
}