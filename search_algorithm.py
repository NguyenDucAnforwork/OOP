from flask import Flask, jsonify, request
import json
import numpy as np
from gensim.models.doc2vec import Doc2Vec, TaggedDocument

app = Flask(__name__)

# Load the data and train the Doc2Vec model
with open('news.json', 'r') as json_file:
    data = json.load(json_file)

tagged_documents = [TaggedDocument(words=document['content'].split(), tags=[str(i)]) for i, document in enumerate(data)]

model = Doc2Vec(tagged_documents, vector_size=784, window=5, min_count=1, workers=4)

# Define the route to serve the K most similar documents
@app.route('/similar_documents', methods=['POST'])
def get_similar_documents():
    # Get the query from the POST request data
    query = request.json.get('query')

    # Embed the query
    query_embedding = model.infer_vector(query.split())

    # Calculate cosine similarity between query and document embeddings
    document_embeddings = np.array([model.infer_vector(document.words) for document in tagged_documents])
    similarities = np.dot(document_embeddings, query_embedding) / (np.linalg.norm(document_embeddings, axis=1) * np.linalg.norm(query_embedding))

    # Get the indices of top K similar documents
    K = int(request.json.get('k', 4))
    top_indices = similarities.argsort()[-K:][::-1]

    # Get the top K similar documents
    similar_documents = [data[int(idx)]['title'] for idx in top_indices]

    return jsonify(similar_documents)

if __name__ == '__main__':
    app.run(debug=True)
