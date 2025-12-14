import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function SweetListPage() {
    const [sweets, setSweets] = useState([]);

    useEffect(() => {
        loadSweets();
    }, []);

    const loadSweets = async () => {
        try {
            const result = await axios.get("http://localhost:8080/api/sweets");
            setSweets(result.data);
        } catch (error) {
            console.error("Error loading sweets:", error);
        }
    };

    return (
        <div className="container mt-5">
            <h2 className="mb-4">Our Sweets</h2>
            <div className="row justify-content-center">
                {sweets.map((sweet) => (
                    <div className="col-md-4 mb-4" key={sweet.id}>
                        <div className="card shadow-sm">
                            <div className="card-body">
                                <h5 className="card-title">{sweet.name}</h5>
                                <h6 className="card-subtitle mb-2 text-muted">{sweet.category}</h6>
                                <p className="card-text">
                                    Price: <strong>${sweet.price}</strong><br/>
                                    Stock: {sweet.quantity}
                                </p>
                                <button className="btn btn-primary w-100">Buy Now</button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}